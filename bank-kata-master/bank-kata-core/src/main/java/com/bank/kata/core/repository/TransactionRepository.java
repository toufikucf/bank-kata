package com.bank.kata.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.bank.kata.core.model.Account;
import com.bank.kata.core.model.Transaction;

import java.util.List;

/**
 * @author toufik youssef
 *
 */
public interface TransactionRepository extends
		JpaRepository<Transaction, Integer>,
		QuerydslPredicateExecutor<Transaction> {
	
	/**
	 * find transactions by account
	 * @param account
	 * @return list of transactions
	 */
	List<Transaction> findByAccount(Account account);

}
