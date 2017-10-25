package com.bank.kata.core.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.kata.core.dto.TransactionDto;
import com.bank.kata.core.model.Account;
import com.bank.kata.core.model.Transaction;
import com.bank.kata.core.repository.AccountRepository;
import com.bank.kata.core.repository.TransactionRepository;
import com.bank.kata.core.service.TransactionService;
import com.bank.kata.core.service.exception.BankException;
import com.bank.kata.core.service.exception.InvalidAccountException;

/**
 * @author toufik youssef
 *
 */
@Service("transactionService")
@Transactional
public class TransactionServiceImpl implements TransactionService {
	
	private final Logger logger = LoggerFactory
			.getLogger(TransactionServiceImpl.class);

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private AccountRepository accountRepository;
	
	private DateFormat df=new SimpleDateFormat("dd/MM/yyyy");

	/* (non-Javadoc)
	 * @see com.bank.kata.core.service.TransactionService#getAllByAccount(java.lang.String)
	 */
	@Override
	public List<TransactionDto> getAllByAccount(String idAccount)
			throws BankException {
		logger.debug("get All Transactions By Account id {}",idAccount);
		Optional<Account> optAccount = accountRepository.findById(idAccount);
		if (optAccount.isPresent()) {
			List<Transaction> transactions = transactionRepository
					.findByAccount(optAccount.get());
			return transactions
					.stream()
					.map(transaction -> new TransactionDto(transaction
							.getAmount(), transaction.getAccountBalance(),
							transaction.getTransactionType(), df.format(transaction
									.getTransactionDate())))
					.collect(Collectors.toList());
		} else {
			logger.error("get All TX By Account id {} failed ",idAccount);
			throw new InvalidAccountException("Account not found");
		}
	}

}
