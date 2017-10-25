package com.bank.kata.core.service;

import java.util.List;

import com.bank.kata.core.dto.TransactionDto;
import com.bank.kata.core.service.exception.BankException;

/**
 * @author toufik Youssef
 *
 */
public interface TransactionService {
	
	/**
	 * retrieve transactions by account ID
	 * @param idAccount
	 * @return list of transctionDto
	 * @throws BankException
	 */
	List<TransactionDto> getAllByAccount(String idAccount) throws BankException;
}
