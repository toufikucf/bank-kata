package com.bank.kata.core.repository;

import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import com.bank.kata.core.commons.AccountBuilder;
import com.bank.kata.core.commons.TransactionBuilder;
import com.bank.kata.core.config.SpringPersistenceConfig;
import com.bank.kata.core.model.Account;
import com.bank.kata.core.model.Transaction;
import com.bank.kata.core.repository.AccountRepository;
import com.bank.kata.core.repository.TransactionRepository;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;

/**
 * @author toufik youssef
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { SpringPersistenceConfig.class }, loader = AnnotationConfigContextLoader.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class,
		TransactionalTestExecutionListener.class,
		DbUnitTestExecutionListener.class })
@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = {
		"/datasets/client/initial.xml", "/datasets/account/initial.xml",
		"/datasets/transaction/initial.xml" })
public class TransactionRepositoryTest {

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private AccountRepository accountRepository;

	@Test
	@DatabaseSetup({ "/datasets/client/initial.xml",
			"/datasets/account/initial.xml",
			"/datasets/transaction/initial.xml" })
	@ExpectedDatabase(value = "/datasets/transaction/final.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
	public void shouldCreateNewTransaction() {
		String accountId = "ACCOUNTID_1";
		TransactionBuilder builder = TransactionBuilder.getBuilder();
		Optional<Account> optAccount = accountRepository.findById(accountId);
		builder.account(optAccount.get());
		Transaction transaction = TransactionBuilder.getBuilder().build();
		transactionRepository.save(transaction);
	}

	@Test
	@DatabaseSetup({ "/datasets/client/initial.xml",
			"/datasets/account/initial.xml",
			"/datasets/transaction/initial.xml" })
	public void shouldfindAllByExistantAccount() {
		Account account = AccountBuilder.getBuilder().build();
		List<Transaction> transactions = transactionRepository
				.findByAccount(account);
		Assert.assertNotNull(transactions);
		Assert.assertEquals(transactions.size(), 4);
		transactions.stream().forEach(
				t -> Assert.assertEquals(t.getAccount().getAccountId(),
						account.getAccountId()));

	}

}
