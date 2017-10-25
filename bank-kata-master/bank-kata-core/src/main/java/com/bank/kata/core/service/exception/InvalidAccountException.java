package com.bank.kata.core.service.exception;

/**
 * @author toufik youssef
 *
 */
public class InvalidAccountException extends BankException{
	
	private static final long serialVersionUID = 1L;

	public InvalidAccountException(String message) {
        super(message);
    }

}
