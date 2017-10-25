package com.bank.kata.core.dto;

import com.bank.kata.core.model.TransactionTypeEnum;

/**
 * @author toufik youssef
 *
 */
public class TransactionDto {
	
	private Double amount;
	private Double accountBalance;
	private TransactionTypeEnum transactionType;
	private String transactionDate;
	
	
	
	public TransactionDto(Double amount, Double accountBalance,
			TransactionTypeEnum transactionType, String transactionDate) {
		super();
		this.amount = amount;
		this.accountBalance = accountBalance;
		this.transactionType = transactionType;
		this.transactionDate = transactionDate;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public Double getAccountBalance() {
		return accountBalance;
	}
	public void setAccountBalance(Double accountBalance) {
		this.accountBalance = accountBalance;
	}
	public TransactionTypeEnum getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(TransactionTypeEnum transactionType) {
		this.transactionType = transactionType;
	}
	public String getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}
	
}
