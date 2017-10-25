package com.bank.kata.core.service;

import java.util.List;

import com.bank.kata.core.dto.AccountDto;
import com.bank.kata.core.model.Account;
import com.bank.kata.core.model.TransactionTypeEnum;
import com.bank.kata.core.service.exception.BankException;

/**
 * @author Toufik Youssef
 *
 */
public interface AccountService {
	
	/**
	 * trasnfert amounts between two accounts by their IDs
	 * @param ribPayee
	 * @param ribPayer
	 * @param amount
	 * @return list of concerned accounts
	 * @throws BankException
	 */
	public List<Account> transfertMoney(String ribPayee, String ribPayer, Double amount) throws BankException;
	
	/**
	 * deposit or withdraw amount from/ for an account 
	 * @param accountId
	 * @param amount
	 * @param transactionTypeEnum
	 * @return concerned account
	 * @throws BankException
	 */
	public Account withDrawDepositMoney(String accountId, Double amount, TransactionTypeEnum transactionTypeEnum) throws BankException;
	
	/**
	 * create a new account 
	 * @param idClient
	 * @param initBalance initial balance 
	 * @return created account
	 * @throws BankException
	 */
	public Account createAccount(String idClient, Double initBalance) throws BankException;
	
	/**
	 * retrieve accounts by client ID
	 * @param idClient
	 * @return accountDto list 
	 */
	public List<AccountDto> getAllAccountsByClient(String idClient);
	
	/**
	 * retrieve all accounts
	 * @return accountDto list
	 */
	public List<AccountDto> getAllAccounts();

}
