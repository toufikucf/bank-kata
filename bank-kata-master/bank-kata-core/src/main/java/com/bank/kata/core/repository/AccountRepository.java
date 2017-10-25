package com.bank.kata.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.bank.kata.core.model.Account;
import com.bank.kata.core.model.Client;

import java.util.List;

/**
 * @author toufik youssef
 *
 */
public interface AccountRepository extends JpaRepository<Account, String>,
		QuerydslPredicateExecutor<Account> {
	
	/**
	 * retrieve list of accounts
	 * @param client
	 * @return list of account
	 */
	List<Account> findByClient(Client client);

}
