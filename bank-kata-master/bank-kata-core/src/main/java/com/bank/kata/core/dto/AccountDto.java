package com.bank.kata.core.dto;

/**
 * @author toufik youssef
 *
 */
public class AccountDto {

	private String accountId;
	private Double balance;
	
	public AccountDto(String accountId, Double balance) {
		super();
		this.accountId = accountId;
		this.balance = balance;
	}
	
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public Double getBalance() {
		return balance;
	}
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	
}
