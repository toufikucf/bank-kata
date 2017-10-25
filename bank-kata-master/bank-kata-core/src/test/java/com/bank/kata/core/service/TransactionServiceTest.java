package com.bank.kata.core.service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.bank.kata.core.commons.TransactionBuilder;
import com.bank.kata.core.dto.TransactionDto;
import com.bank.kata.core.model.Transaction;
import com.bank.kata.core.model.TransactionTypeEnum;
import com.bank.kata.core.repository.AccountRepository;
import com.bank.kata.core.repository.TransactionRepository;
import com.bank.kata.core.service.exception.BankException;
import com.bank.kata.core.service.impl.TransactionServiceImpl;

/**
 * @author toufik youssef
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class TransactionServiceTest {

	@InjectMocks
	private TransactionServiceImpl transactionService;

	@Mock
	private TransactionRepository transactionRepository;

	@Mock
	private AccountRepository accountRepository;

	@Test
	public void shouldGetAllTransactionsByValidAccount() throws BankException {
		Transaction transaction = TransactionBuilder.getBuilder().build();
		List<Transaction> transactions = Arrays.asList(transaction,
				new Transaction(transaction.getAccount(), 500d, 3000d,
						TransactionTypeEnum.DEPOSIT, new Date()));
		Mockito.when(
				transactionRepository.findByAccount(transaction.getAccount()))
				.thenReturn(transactions);
		Mockito.when(
				accountRepository.findById(transaction.getAccount()
						.getAccountId())).thenReturn(
				Optional.of(transaction.getAccount()));

		List<TransactionDto> listsAsDto = transactionService
				.getAllByAccount(transaction.getAccount().getAccountId());
		Assert.assertNotNull(listsAsDto);
		Assert.assertEquals(listsAsDto.size(), 2);

	}

}
