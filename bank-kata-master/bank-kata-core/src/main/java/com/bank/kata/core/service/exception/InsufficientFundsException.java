package com.bank.kata.core.service.exception;

/**
 * @author toufik youssef
 *
 */
public class InsufficientFundsException extends BankException{
	
	private static final long serialVersionUID = 1L;

	public InsufficientFundsException(String message) {
        super(message);
    }

}
