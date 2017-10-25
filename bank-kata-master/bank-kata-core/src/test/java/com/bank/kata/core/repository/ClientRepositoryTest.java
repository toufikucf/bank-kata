package com.bank.kata.core.repository;

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

import com.bank.kata.core.config.SpringPersistenceConfig;
import com.bank.kata.core.model.Client;
import com.bank.kata.core.repository.ClientRepository;
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
public class ClientRepositoryTest {

	@Autowired
	private ClientRepository clientRepository;

	@Test
	@DatabaseSetup({ "/datasets/client/initial.xml",
			"/datasets/account/initial.xml",
			"/datasets/transaction/initial.xml" })
	@ExpectedDatabase(value = "/datasets/client/final.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
	public void shouldCreateNewAccount() {
		Client client = new Client();
		client.setClientId("clientid_4");
		client.setName("client4");
		client.setPwd("pwd4");
		clientRepository.save(client);
	}

	@Test
	@DatabaseSetup({ "/datasets/client/initial.xml",
			"/datasets/account/initial.xml",
			"/datasets/transaction/initial.xml" })
	public void shouldfindByExistantClientID() {
		Optional<Client> optClient = clientRepository.findById("clientid_1");
		Assert.assertEquals(optClient.isPresent(), true);
		Assert.assertEquals(optClient.get().getClientId(), "clientid_1");

	}

	@Test
	@DatabaseSetup({ "/datasets/client/initial.xml",
			"/datasets/account/initial.xml",
			"/datasets/transaction/initial.xml" })
	public void shouldfindNoneByUnExistantClientID() {
		Optional<Client> optClient = clientRepository.findById("clientid_4");
		Assert.assertEquals(optClient.isPresent(), false);
	}

}
