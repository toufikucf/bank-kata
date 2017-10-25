package com.bank.kata.web.acceptance;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

import com.bank.kata.core.config.SpringPersistenceConfig;
import com.bank.kata.core.config.SpringRootConfig;
import com.bank.kata.core.dto.TransactionDto;
import com.bank.kata.web.config.SpringWebConfig;
import com.bank.kata.web.contoller.BankingController;
import com.bank.kata.web.dto.GenericApiResponseDto;
import com.bank.kata.web.dto.TransfertDto;
import com.bank.kata.web.dto.WithDrawDepositDto;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.annotation.ExpectedDatabases;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;

/**
 * @author toufik youssef
 *
 */
@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { SpringPersistenceConfig.class,
		SpringWebConfig.class, SpringRootConfig.class }, loader = AnnotationConfigWebContextLoader.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class,
		TransactionalTestExecutionListener.class,
		DbUnitTestExecutionListener.class })
@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = {
		"/datasets/acceptance/initial/account/initial.xml",
		"/datasets/acceptance/initial/transaction/initial.xml" })
public class BankingControllerTest {

	@Autowired
	private WebApplicationContext webContext;

	@Autowired
	private BankingController bankingController;

	@Test
	@DatabaseSetup({ "/datasets/acceptance/initial/client/initial.xml",
			"/datasets/acceptance/initial/account/initial.xml",
			"/datasets/acceptance/initial/transaction/initial.xml" })
	@ExpectedDatabases({
			@ExpectedDatabase(value = "/datasets/acceptance/withDraw/account/final.xml", assertionMode = DatabaseAssertionMode.NON_STRICT),
			@ExpectedDatabase(value = "/datasets/acceptance/withDraw/transaction/final.xml", assertionMode = DatabaseAssertionMode.NON_STRICT) })
	public void clientCanWithDrawAmount() {

		WithDrawDepositDto withDrawDepositDto = new WithDrawDepositDto(
				"ACCOUNTID_1", 200d);
		GenericApiResponseDto genericApiResponseDto = bankingController
				.withDrawAmount(withDrawDepositDto);
		Assert.assertTrue(genericApiResponseDto.getStatus());
		Assert.assertNotNull(genericApiResponseDto.getAccountDto());
		Assert.assertEquals(genericApiResponseDto.getAccountDto()
				.getAccountId(), "ACCOUNTID_1");
		Assert.assertEquals(genericApiResponseDto.getAccountDto().getBalance(),
				Double.valueOf(4800d));
	}

	@Test
	@DatabaseSetup({ "/datasets/acceptance/initial/client/initial.xml",
			"/datasets/acceptance/initial/account/initial.xml",
			"/datasets/acceptance/initial/transaction/initial.xml" })
	public void clientCannotWithDrawAmount() {

		WithDrawDepositDto withDrawDepositDto = new WithDrawDepositDto(
				"ACCOUNTID_1", 100000d);
		GenericApiResponseDto genericApiResponseDto = bankingController
				.withDrawAmount(withDrawDepositDto);
		Assert.assertFalse(genericApiResponseDto.getStatus());
		Assert.assertNull(genericApiResponseDto.getAccountDto());
		Assert.assertTrue(genericApiResponseDto.getMessage().length() > 0);
	}

	@Test
	@DatabaseSetup({ "/datasets/acceptance/initial/client/initial.xml",
			"/datasets/acceptance/initial/account/initial.xml",
			"/datasets/acceptance/initial/transaction/initial.xml" })
	@ExpectedDatabases({
			@ExpectedDatabase(value = "/datasets/acceptance/deposit/account/final.xml", assertionMode = DatabaseAssertionMode.NON_STRICT),
			@ExpectedDatabase(value = "/datasets/acceptance/deposit/transaction/final.xml", assertionMode = DatabaseAssertionMode.NON_STRICT) })
	public void clientCanDepositAmount() {

		WithDrawDepositDto withDrawDepositDto = new WithDrawDepositDto(
				"ACCOUNTID_1", 500d);
		GenericApiResponseDto genericApiResponseDto = bankingController
				.depositAmount(withDrawDepositDto);
		Assert.assertTrue(genericApiResponseDto.getStatus());
		Assert.assertNotNull(genericApiResponseDto.getAccountDto());
		Assert.assertEquals(genericApiResponseDto.getAccountDto()
				.getAccountId(), "ACCOUNTID_1");
		Assert.assertEquals(genericApiResponseDto.getAccountDto().getBalance(),
				Double.valueOf(5500d));
	}

	@Test
	@DatabaseSetup({ "/datasets/acceptance/initial/client/initial.xml",
			"/datasets/acceptance/initial/account/initial.xml",
			"/datasets/acceptance/initial/transaction/initial.xml" })
	public void clientCanNotDepositAmount() {

		WithDrawDepositDto withDrawDepositDto = new WithDrawDepositDto(
				"ACCOUNTID_1", -500d);
		GenericApiResponseDto genericApiResponseDto = bankingController
				.depositAmount(withDrawDepositDto);
		Assert.assertFalse(genericApiResponseDto.getStatus());
		Assert.assertNull(genericApiResponseDto.getAccountDto());
		Assert.assertTrue(genericApiResponseDto.getMessage().length() > 0);
	}

	@Test
	@DatabaseSetup({ "/datasets/acceptance/initial/client/initial.xml",
			"/datasets/acceptance/initial/account/initial.xml",
			"/datasets/acceptance/initial/transaction/initial.xml" })
	@ExpectedDatabases({
			@ExpectedDatabase(value = "/datasets/acceptance/transfert/account/final.xml", assertionMode = DatabaseAssertionMode.NON_STRICT),
			@ExpectedDatabase(value = "/datasets/acceptance/transfert/transaction/final.xml", assertionMode = DatabaseAssertionMode.NON_STRICT) })
	public void clientCanTransfertToAccountt() {

		TransfertDto transfertDto = new TransfertDto("ACCOUNTID_1",
				"ACCOUNTID_2", 500d);
		GenericApiResponseDto genericApiResponseDto = bankingController
				.transfertAmount(transfertDto);
		Assert.assertTrue(genericApiResponseDto.getStatus());
		Assert.assertNotNull(genericApiResponseDto.getAccountDto());
		Assert.assertEquals(genericApiResponseDto.getAccountDto()
				.getAccountId(), "ACCOUNTID_1");
		Assert.assertEquals(genericApiResponseDto.getAccountDto().getBalance(),
				Double.valueOf(4500d));
	}
	
	@Test
	@DatabaseSetup({ "/datasets/acceptance/initial/client/initial.xml",
			"/datasets/acceptance/initial/account/initial.xml",
			"/datasets/acceptance/initial/transaction/initial.xml" })
	public void clientCanNotTransfertFromToTheSameAccount() {

		TransfertDto transfertDto = new TransfertDto("ACCOUNTID_1",
				"ACCOUNTID_1", 500d);
		GenericApiResponseDto genericApiResponseDto = bankingController
				.transfertAmount(transfertDto);
		Assert.assertFalse(genericApiResponseDto.getStatus());
		Assert.assertNull(genericApiResponseDto.getAccountDto());
		Assert.assertTrue(genericApiResponseDto.getMessage().length() > 0);
	}
	
	@Test
	@DatabaseSetup({ "/datasets/acceptance/initial/client/initial.xml",
			"/datasets/acceptance/initial/account/initial.xml",
			"/datasets/acceptance/initial/transaction/initial.xml" })
	public void clientCanRetrieveTransfertsFromToAccount() {

		String AccountId = "ACCOUNTID_1";

		List<TransactionDto> transactions = bankingController
				.getTransactionByAccount(AccountId);
		Assert.assertNotNull(transactions);
		Assert.assertEquals(transactions.size(), 4);
	}

}
