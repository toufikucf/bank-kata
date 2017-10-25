package com.bank.kata.core.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.kata.core.dto.AccountDto;
import com.bank.kata.core.model.Account;
import com.bank.kata.core.model.Client;
import com.bank.kata.core.model.Transaction;
import com.bank.kata.core.model.TransactionTypeEnum;
import com.bank.kata.core.repository.AccountRepository;
import com.bank.kata.core.repository.ClientRepository;
import com.bank.kata.core.repository.TransactionRepository;
import com.bank.kata.core.service.AccountService;
import com.bank.kata.core.service.exception.BankException;
import com.bank.kata.core.service.exception.InsufficientFundsException;
import com.bank.kata.core.service.exception.InvalidAccountException;
import com.bank.kata.core.service.exception.InvalidAmountException;
import com.bank.kata.core.service.exception.InvalidClientDataException;
import com.bank.kata.core.service.exception.SameAccountException;

/**
 * @author toufik youssef
 *
 */
@Service("accountService")
@Transactional
public class AccountServiceImpl implements AccountService {

	private final Logger logger = LoggerFactory
			.getLogger(AccountServiceImpl.class);

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private ClientRepository clientRepository;

	/* (non-Javadoc)
	 * @see com.bank.kata.core.service.AccountService#transfertMoney(java.lang.String, java.lang.String, java.lang.Double)
	 */
	@Override
	public List<Account> transfertMoney(String ribPayee, String ribPayer,
			Double amount) throws BankException {
		logger.debug(
				"transfert amount {} from account id {} to account id {} ",
				amount, ribPayer, ribPayee);
		if (!checkAmountValidity(amount)) {
			logger.error(
					"transfert amount {} from account id {} to account id {} failed: Invalid amount",
					amount, ribPayer, ribPayee);
			throw new InvalidAmountException("INVALID AMOUNT");
		}
		if (ribPayer.equals(ribPayee)) {
			logger.error(
					"transfert amount {} from account id {} to account id {} failed: cannot transfert money from/to the same account",
					amount, ribPayer, ribPayee);
			throw new SameAccountException(
					"Cannot transfert money from/to the same account");
		}

		Optional<Account> optPayeeAccount = accountRepository
				.findById(ribPayee);
		Optional<Account> optPayerAccount = accountRepository
				.findById(ribPayer);
		if (optPayeeAccount.isPresent() && optPayerAccount.isPresent()) {
			Account payee = optPayeeAccount.get();
			Account payer = optPayerAccount.get();
			if (payer.getBalance() < amount) {
				throw new InsufficientFundsException(
						"Insufficient Funds for this transaction");
			}
			payer.setBalance(payer.getBalance() - amount);
			payee.setBalance(payee.getBalance() + amount);

			Transaction payerTransaction = new Transaction(payer, amount,
					payer.getBalance(), TransactionTypeEnum.DEBIT, new Date());

			Transaction payeeTransaction = new Transaction(payee, amount,
					payee.getBalance(), TransactionTypeEnum.CREDIT, new Date());
			List<Transaction> listTx = Arrays.asList(payeeTransaction,
					payerTransaction);
			List<Account> listAccount = Arrays.asList(payer, payee);
			transactionRepository.saveAll(listTx);
			return accountRepository.saveAll(listAccount);

		} else {
			logger.error(
					"transfert amount {} from account id {} to account id {} failed: PAYEE OR PAYER IS NOT VALID",
					amount, ribPayer, ribPayee);
			throw new InvalidAccountException("PAYEE OR PAYER IS NOT VALID");
		}

	}

	/* (non-Javadoc)
	 * @see com.bank.kata.core.service.AccountService#withDrawDepositMoney(java.lang.String, java.lang.Double, com.bank.kata.core.model.TransactionTypeEnum)
	 */
	@Override
	public Account withDrawDepositMoney(String accountId, Double amount,
			TransactionTypeEnum transactionTypeEnum) throws BankException {
		logger.debug("operation {} with amount {} for account id {} ",
				transactionTypeEnum, amount, accountId);

		if (!checkAmountValidity(amount)) {
			logger.error(
					"Operation {} with amount {} for account id {} failed: INVALID AMOUNT",
					transactionTypeEnum, amount, accountId);
			throw new InvalidAmountException("INVALID AMOUNT");
		}

		Optional<Account> optAccount = accountRepository.findById(accountId);
		if (optAccount.isPresent()) {
			Account account = optAccount.get();
			if (transactionTypeEnum == TransactionTypeEnum.DEPOSIT) {
				account = depositAmount(account, amount);
			} else {
				account = withDrawAmount(account, amount);
			}
			Transaction withDrawTransaction = new Transaction(account, amount,
					account.getBalance(), transactionTypeEnum, new Date());

			accountRepository.save(account);
			transactionRepository.save(withDrawTransaction);
			return account;
		} else {
			logger.error(
					"Operation {} with amount {} for account id {} failed: INVALID Account",
					transactionTypeEnum, amount, accountId);
			throw new InvalidAccountException("Account IS NOT VALID");
		}

	}

	/* (non-Javadoc)
	 * @see com.bank.kata.core.service.AccountService#createAccount(java.lang.String, java.lang.Double)
	 */
	@Override
	public Account createAccount(String idClient, Double initBalance)
			throws BankException {

		logger.debug("Create account with ID {} and init balance {} ",
				idClient, initBalance);
		Optional<Client> optClient = clientRepository.findById(idClient);
		if (!optClient.isPresent() || initBalance <= 0) {
			logger.error(
					"Creation for account with ID {} and init balance {} failed ",
					idClient, initBalance);
			throw new InvalidClientDataException(
					"Cannot create account due to wrong data");
		} else {
			Account account = new Account();
			account.setClient(optClient.get());
			account.setBalance(initBalance);
			account.setAccountId(accountIdGenerator(idClient));
			return accountRepository.save(account);
		}
	}

	/* (non-Javadoc)
	 * @see com.bank.kata.core.service.AccountService#getAllAccountsByClient(java.lang.String)
	 */
	@Override
	public List<AccountDto> getAllAccountsByClient(String idClient) {
		logger.debug("Retrieve accounts by client ID {}  ", idClient);
		List<AccountDto> accountsListAsDto = new ArrayList<>();
		Optional<Client> optClient = clientRepository.findById(idClient);
		if (optClient.isPresent()) {
			List<Account> accounts = accountRepository.findByClient(optClient
					.get());
			accountsListAsDto = accounts
					.stream()
					.map(account -> new AccountDto(account.getAccountId(),
							account.getBalance())).collect(Collectors.toList());
		}
		return accountsListAsDto;
	}

	/* (non-Javadoc)
	 * @see com.bank.kata.core.service.AccountService#getAllAccounts()
	 */
	@Override
	public List<AccountDto> getAllAccounts() {
		logger.debug("Retrieve all accounts ");
		List<AccountDto> accountsListAsDto;
		List<Account> accounts = accountRepository.findAll();
		accountsListAsDto = accounts
				.stream()
				.map(account -> new AccountDto(account.getAccountId(), account
						.getBalance())).collect(Collectors.toList());
		return accountsListAsDto;
	}

	/**
	 * Check wether an amout is valid or not
	 * @param amount
	 * @return boolean 
	 */
	private boolean checkAmountValidity(Double amount) {
		boolean checkValue;
		if (null == amount || amount <= 0) {
			checkValue = false;
		} else {
			checkValue = true;
		}
		return checkValue;
	}

	/**
	 * auto generation of account id
	 * @param cliendId
	 * @return an ID 
	 */
	private String accountIdGenerator(String cliendId) {
		StringBuilder builder = new StringBuilder();
		builder.append(cliendId).append("_").append(new Date().getTime());
		return builder.toString();
	}

	/**
	 * do withdraw process
	 * @param account
	 * @param amount
	 * @return concerned account
	 * @throws BankException
	 */
	private Account withDrawAmount(Account account, Double amount)
			throws BankException {
		if (account.getBalance() < amount) {
			throw new InsufficientFundsException(
					"Insufficient Funds for this operation");
		}
		account.setBalance(account.getBalance() - amount);
		return account;
	}

	/**
	 * do deposit process
	 * @param account
	 * @param amount
	 * @return concerned account
	 */
	private Account depositAmount(Account account, Double amount) {
		account.setBalance(account.getBalance() + amount);
		return account;
	}

}
