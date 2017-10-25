package com.bank.kata.core.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * @author Youssef Toufik
 *
 */
@Entity
public class Transaction {
	
	private Integer idTransaction;
	private Account account;
	private Double amount;
	private Double accountBalance;
	private TransactionTypeEnum transactionType;
	private Date transactionDate;
	
	/**
	 * Default constructor
	 */
	public Transaction() {
		super();
	}
	
	/**
	 * All fields Transaction constructor.
	 * 
	 * @param account
	 * @param amount
	 * @param accountBalance
	 * @param transactionType
	 * @param transactionDate
	 */
	public Transaction( Account account,
			Double amount, Double accountBalance,
			TransactionTypeEnum transactionType, Date transactionDate) {
		super();
		this.account = account;
		this.amount = amount;
		this.accountBalance = accountBalance;
		this.transactionType = transactionType;
		this.transactionDate = transactionDate;
	}
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer getIdTransaction() {
		return idTransaction;
	}
	public void setIdTransaction(Integer idTransaction) {
		this.idTransaction = idTransaction;
	}
	
	@ManyToOne()
	@JoinColumn(name = "ACCOUNTID")
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
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
	@Enumerated(EnumType.STRING)
	public TransactionTypeEnum getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(TransactionTypeEnum transactionType) {
		this.transactionType = transactionType;
	}
	@Column(nullable=false)
	public Date getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}
	
	
}
