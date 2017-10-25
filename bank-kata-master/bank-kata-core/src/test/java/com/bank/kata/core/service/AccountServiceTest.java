package com.bank.kata.core.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.bank.kata.core.commons.AccountBuilder;
import com.bank.kata.core.dto.AccountDto;
import com.bank.kata.core.model.Account;
import com.bank.kata.core.model.Client;
import com.bank.kata.core.model.TransactionTypeEnum;
import com.bank.kata.core.repository.AccountRepository;
import com.bank.kata.core.repository.ClientRepository;
import com.bank.kata.core.repository.TransactionRepository;
import com.bank.kata.core.service.exception.BankException;
import com.bank.kata.core.service.exception.InsufficientFundsException;
import com.bank.kata.core.service.exception.InvalidAmountException;
import com.bank.kata.core.service.exception.InvalidClientDataException;
import com.bank.kata.core.service.impl.AccountServiceImpl;

/**
 * @author toufik youssef
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest {

	@InjectMocks
	private AccountServiceImpl accountService;
	@Mock
	private TransactionRepository transactionRepository;
	@Mock
	private AccountRepository accountRepository;
	@Mock
	private ClientRepository clientRepository;

	@Test
	public void shouldAddNewAccoutWhenValidData() throws BankException {
		Mockito.when(accountRepository.save(Mockito.any(Account.class)))
				.thenReturn(AccountBuilder.getBuilder().build());
		Mockito.when(clientRepository.findById(Mockito.any(String.class)))
				.thenReturn(
						Optional.of(AccountBuilder.getBuilder().buildClient()));
		String idClient = "clientid_1";
		double initBalance = 5000d;
		Account account = accountService.createAccount(idClient, initBalance);
		Assert.assertEquals(account.getClient().getClientId(), idClient);
		Assert.assertEquals(account.getBalance(), Double.valueOf(initBalance));
	}

	@Test(expected = InvalidClientDataException.class)
	public void addAccoutShouldThorwExceptionWhenWrongData()
			throws BankException {
		String wrongIdClient = "someWrongIdClient";
		Mockito.when(clientRepository.findById(wrongIdClient)).thenReturn(
				Optional.empty());
		double initBalance = 1000d;
		accountService.createAccount(wrongIdClient, initBalance);
	}

	@Test
	public void shouldTransfertMoneyWhenValidData() throws BankException {
		Account payee = AccountBuilder.getBuilder().build();
		Account payer = new Account("acountid_2", 1000d, payee.getClient());
		Double amount = 50d;
		Mockito.when(accountRepository.findById(payee.getAccountId()))
				.thenReturn(Optional.of(payee));
		Mockito.when(accountRepository.findById(payer.getAccountId()))
				.thenReturn(Optional.of(payer));
		List<Account> resultedAccounts = accountService.transfertMoney(
				payee.getAccountId(), payer.getAccountId(), amount);
		resultedAccounts
				.stream()
				.forEach(
						account -> {
							if (account.getAccountId() == payee.getAccountId()) {
								Assert.assertTrue(payee.getBalance() + amount == account
										.getBalance());
							} else {
								Assert.assertTrue(payee.getBalance() - amount == account
										.getBalance());
							}
						});

	}

	@Test(expected = InsufficientFundsException.class)
	public void shouldThrowExceptionWhenAmountexceedsBalance()
			throws BankException {
		Account payee = AccountBuilder.getBuilder().build();
		Account payer = new Account("acountid_2", 1000d, payee.getClient());
		Double amount = 50000000d;
		Mockito.when(accountRepository.findById(payee.getAccountId()))
				.thenReturn(Optional.of(payee));
		Mockito.when(accountRepository.findById(payer.getAccountId()))
				.thenReturn(Optional.of(payer));
		accountService.transfertMoney(payee.getAccountId(),
				payer.getAccountId(), amount);
	}

	@Test
	public void shouldDeposit() throws BankException {
		Account account = AccountBuilder.getBuilder().build();
		double initAmount = account.getBalance();
		Double amount = 200d;
		Mockito.when(accountRepository.findById(account.getAccountId()))
				.thenReturn(Optional.of(account));
		accountService.withDrawDepositMoney(account.getAccountId(), amount,
				TransactionTypeEnum.DEPOSIT);
		Assert.assertEquals(account.getBalance(),
				Double.valueOf(initAmount + amount));
	}
	
	@Test(expected=InvalidAmountException.class)
	public void depositShouldThrowExceptionWhenInvalidAmount() throws BankException {
		Account account = AccountBuilder.getBuilder().build();
		Double amount = -200d;
		accountService.withDrawDepositMoney(account.getAccountId(), amount,
				TransactionTypeEnum.DEPOSIT);
	}

	@Test(expected = InsufficientFundsException.class)
	public void withDrawShouldThrowExceptionWhenAmountexceedsBalance()
			throws BankException {
		Account account = AccountBuilder.getBuilder().build();
		Double amount = 5000000d;
		Mockito.when(accountRepository.findById(account.getAccountId()))
				.thenReturn(Optional.of(account));
		accountService.withDrawDepositMoney(account.getAccountId(), amount,
				TransactionTypeEnum.WITHDRAW);
	}

	@Test()
	public void shouldgetAllAccountsByClient() {
		Account account = AccountBuilder.getBuilder().build();
		Client client= AccountBuilder.getBuilder().buildClient();
		List<Account> accounts= Arrays.asList(account, new Account("accountId_2", 1000d, account.getClient()));
		
		Mockito.when(accountRepository.findByClient(Mockito.any(Client.class)))
				.thenReturn(accounts);
		Mockito.when(clientRepository.findById(Mockito.any(String.class))).thenReturn(Optional.of(client));
		
		List<AccountDto> accountsAsDto=accountService.getAllAccountsByClient(account.getClient().getClientId());
		Assert.assertNotNull(accountsAsDto);
		Assert.assertEquals(accountsAsDto.size(), 2);
	}
	
	@Test()
	public void shouldgetAllAccounts() {
		Account account = AccountBuilder.getBuilder().build();
		List<Account> accounts= Arrays.asList(account, new Account("accountId_2", 1000d, account.getClient()));
		
		Mockito.when(accountRepository.findAll())
				.thenReturn(accounts);
		
		List<AccountDto> accountsAsDto=accountService.getAllAccounts();
		Assert.assertNotNull(accountsAsDto);
		Assert.assertEquals(accountsAsDto.size(), 2);
	}
}
