package com.bank.kata.core.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 * @author toufik youssef
 *
 */
@Entity
public class Account {
	
	private String accountId;
	private Double balance;
	private Client client;
	private Set<Transaction> transactions= new HashSet<>();
	
	public Account(String accountId, Double balance, Client client) {
		super();
		this.accountId = accountId;
		this.balance = balance;
		this.client = client;
	}

	public Account() {
		super();
	}

	@Id
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
	
	@ManyToOne()
	@JoinColumn(name = "CLIENTID")
	public Client getClient() {
		return client;
	}
	
	public void setClient(Client client) {
		this.client = client;
	}
	
	@OneToMany(mappedBy="account")
	public Set<Transaction> getTransactions() {
		return transactions;
	}
	
	public void setTransactions(Set<Transaction> transactions) {
		this.transactions = transactions;
	}
	
	

}
