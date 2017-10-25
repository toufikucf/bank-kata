package com.bank.kata.web.contoller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bank.kata.core.dto.AccountDto;
import com.bank.kata.core.dto.TransactionDto;
import com.bank.kata.core.model.Account;
import com.bank.kata.core.model.TransactionTypeEnum;
import com.bank.kata.core.service.AccountService;
import com.bank.kata.core.service.TransactionService;
import com.bank.kata.core.service.exception.BankException;
import com.bank.kata.web.dto.GenericApiResponseDto;
import com.bank.kata.web.dto.TransfertDto;
import com.bank.kata.web.dto.WithDrawDepositDto;

/**
 * @author toufik youssef
 *
 */
@RestController
@RequestMapping("/rest")
public class BankingController {

	private final Logger logger = LoggerFactory
			.getLogger(BankingController.class);

	@Autowired
	private AccountService accountService;

	@Autowired
	private TransactionService transactionService;

	/**
	 * 
	 * @param withDrawDepositDto
	 * @return GenericApiResponseDto
	 */
	@RequestMapping(value = "/with-draw-amount", method = RequestMethod.POST)
	public GenericApiResponseDto withDrawAmount(
			@RequestBody WithDrawDepositDto withDrawDepositDto) {

		logger.debug("calling with-draw-amount with data {}: ",
				withDrawDepositDto);
		GenericApiResponseDto withDrawResponse = new GenericApiResponseDto();
		try {
			Account account = accountService.withDrawDepositMoney(
					withDrawDepositDto.getAccountId(),
					withDrawDepositDto.getAmount(),
					TransactionTypeEnum.WITHDRAW);
			withDrawResponse.setStatus(true);
			withDrawResponse.setAccountDto(new AccountDto(account
					.getAccountId(), account.getBalance()));
		} catch (BankException e) {
			logger.error("calling with-draw-amount with data {} failled: ",
					withDrawDepositDto);
			withDrawResponse.setMessage(e.getMessage());
			withDrawResponse.setStatus(false);
		}
		return withDrawResponse;
	}

	/**
	 * @param withDrawDepositDto
	 * @return GenericApiResponseDto
	 */
	@RequestMapping(value = "/deposit-amount", method = RequestMethod.POST)
	public GenericApiResponseDto depositAmount(
			@RequestBody WithDrawDepositDto withDrawDepositDto) {
		logger.debug("calling deposit-amount with data {}: ", withDrawDepositDto);
		GenericApiResponseDto withDrawResponse = new GenericApiResponseDto();
		try {
			Account account = accountService
					.withDrawDepositMoney(withDrawDepositDto.getAccountId(),
							withDrawDepositDto.getAmount(),
							TransactionTypeEnum.DEPOSIT);
			withDrawResponse.setStatus(true);
			withDrawResponse.setAccountDto(new AccountDto(account
					.getAccountId(), account.getBalance()));
		} catch (BankException e) {
			logger.error("calling deposit-amount with data {} failled: ",
					withDrawDepositDto);
			withDrawResponse.setMessage(e.getMessage());
			withDrawResponse.setStatus(false);
		}
		return withDrawResponse;
	}

	/**
	 * @param idAccount
	 * @return GenericApiResponseDto
	 */
	@RequestMapping(value = "/transaction-list/{idAccount}", method = RequestMethod.GET)
	public List<TransactionDto> getTransactionByAccount(
			@PathVariable String idAccount) {
		logger.debug("calling transaction-lis for account {}: ", idAccount);
		List<TransactionDto> listTransaction = new ArrayList<>();
		try {
			listTransaction = transactionService.getAllByAccount(idAccount);
		} catch (BankException e) {
			logger.error("calling transaction-list failled for account ",
					idAccount);
		}
		return listTransaction;
	}

	/**
	 * @return accountsDto list 
	 */
	@RequestMapping(value = "/accounts-list", method = RequestMethod.GET)
	public List<AccountDto> retrieveAccountsList() {
		logger.debug("Retrieving accounts list ");
		return  accountService.getAllAccounts();
	}

	/**
	 * @param transfertDto
	 * @return GenericApiResponseDto
	 */
	@RequestMapping(value = "/transfert-amount", method = RequestMethod.POST)
	public GenericApiResponseDto transfertAmount(
			@RequestBody TransfertDto transfertDto) {

		logger.debug("calling transfert-amountt with data {}: ", transfertDto);
		GenericApiResponseDto withDrawResponse = new GenericApiResponseDto();
		try {
			List<Account> result = accountService.transfertMoney(
					transfertDto.getPayeeId(), transfertDto.getPayerId(),
					transfertDto.getAmount());
			withDrawResponse.setStatus(true);
			Optional<Account> optPayer = result
					.stream()
					.filter(account -> account.getAccountId().equals(
							transfertDto.getPayerId())).findAny();

			Double updatedBalance = optPayer.isPresent() ? optPayer.get()
					.getBalance() : null;
			withDrawResponse.setAccountDto(new AccountDto(transfertDto
					.getPayerId(), updatedBalance));
		} catch (BankException e) {
			logger.error("calling with-draw-amount with data {} failled: ",
					transfertDto);
			withDrawResponse.setMessage(e.getMessage());
			withDrawResponse.setStatus(false);
		}
		return withDrawResponse;
	}

}