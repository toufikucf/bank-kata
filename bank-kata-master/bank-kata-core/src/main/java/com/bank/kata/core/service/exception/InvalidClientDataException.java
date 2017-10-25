package com.bank.kata.core.service.exception;

/**
 * @author toufik youssef
 *
 */
public class InvalidClientDataException extends BankException{

	private static final long serialVersionUID = 1L;
	
	public InvalidClientDataException(String message) {
        super(message);
    }

}
