package com.bank.kata.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.bank.kata.core.model.Client;

/**
 * @author toufik youssef
 *
 */
public interface ClientRepository extends JpaRepository<Client, String>,
		QuerydslPredicateExecutor<Client> {

}
