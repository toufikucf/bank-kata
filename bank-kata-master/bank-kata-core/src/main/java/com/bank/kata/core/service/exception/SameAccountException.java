package com.bank.kata.core.service.exception;

/**
 * @author toufik youssef
 * Same account Exception
 */
public class SameAccountException extends BankException{

	private static final long serialVersionUID = 1L;

	public SameAccountException(String message) {
        super(message);
    }
}
