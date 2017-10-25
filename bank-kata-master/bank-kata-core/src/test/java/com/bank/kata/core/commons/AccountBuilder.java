package com.bank.kata.core.commons;

import com.bank.kata.core.model.Account;
import com.bank.kata.core.model.Client;

/**
 * @author toufik youssef
 *
 */
public class AccountBuilder {

	private Client client = new Client("clientid_1", "client1", "pwd1");;

	public static AccountBuilder getBuilder() {
		return new AccountBuilder();
	}

	public Client buildClient() {
		return client;
	}

	public Account build() {
		return new Account("ACCOUNTID_1", 5000d, client);
	}

	public AccountBuilder client(Client client) {
		this.client = client;
		return this;
	}
	
	

}
