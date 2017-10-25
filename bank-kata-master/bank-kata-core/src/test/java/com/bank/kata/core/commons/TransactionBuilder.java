package com.bank.kata.core.commons;

import java.util.Date;

import com.bank.kata.core.model.Account;
import com.bank.kata.core.model.Transaction;
import com.bank.kata.core.model.TransactionTypeEnum;

/**
 * @author toufik youssef
 *
 */
public class TransactionBuilder {

	private Account account = AccountBuilder.getBuilder().build();

	public static TransactionBuilder getBuilder() {
		return new TransactionBuilder();
	}

	public Transaction build() {
		return new Transaction(account, 200d, 5000d,
				TransactionTypeEnum.DEPOSIT, new Date());
	}

	public TransactionBuilder account(Account account) {
		this.account = account;
		return this;
	}

}
