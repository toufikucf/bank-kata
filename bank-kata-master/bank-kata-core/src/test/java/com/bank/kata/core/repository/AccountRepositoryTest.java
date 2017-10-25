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
import com.bank.kata.core.config.SpringPersistenceConfig;
import com.bank.kata.core.model.Account;
import com.bank.kata.core.model.Client;
import com.bank.kata.core.repository.AccountRepository;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

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
public class AccountRepositoryTest {

	@Autowired
	private AccountRepository accountRepository;

	@Test
	@DatabaseSetup({ "/datasets/client/initial.xml",
			"/datasets/account/initial.xml",
			"/datasets/transaction/initial.xml" })
	public void shouldFindAccountByExistantClient() {
		Client client = AccountBuilder.getBuilder().buildClient();
		List<Account> accountsByClient = accountRepository.findByClient(client);
		Assert.assertNotNull(accountsByClient);
		Assert.assertEquals(accountsByClient.size(), 2);
		Assert.assertEquals(accountsByClient.get(0).getClient().getClientId(),
				client.getClientId());
		Assert.assertEquals(accountsByClient.get(0).getBalance(),
				Double.valueOf(5000d));
		Assert.assertEquals(accountsByClient.get(1).getBalance(),
				Double.valueOf(3000d));
	}

	@Test
	@DatabaseSetup({ "/datasets/client/initial.xml",
			"/datasets/account/initial.xml",
			"/datasets/transaction/initial.xml" })
	public void shouldFindAccountByCorrectId() {
		String idAccount = "ACCOUNTID_1";
		Optional<Account> optAccount = accountRepository.findById(idAccount);
		Assert.assertEquals(optAccount.isPresent(), true);
		Assert.assertEquals(optAccount.get().getAccountId(), idAccount);
	}

	@Test
	@DatabaseSetup({ "/datasets/client/initial.xml",
			"/datasets/account/initial.xml",
			"/datasets/transaction/initial.xml" })
	public void shouldFindAllAccounts() {
		List<Account> accounts = accountRepository.findAll();
		Assert.assertNotNull(accounts);
		Assert.assertEquals(accounts.size(), 4);
	}

}
