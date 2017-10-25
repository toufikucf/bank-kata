package com.bank.kata.core.service.exception;

/**
 * @author toufik youssef
 *
 */
public class InvalidAmountException extends BankException{
	
	private static final long serialVersionUID = 1L;

	public InvalidAmountException(String message) {
        super(message);
    }

}
