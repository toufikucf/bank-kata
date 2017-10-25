package com.bank.kata.web.dto;

import com.bank.kata.core.dto.AccountDto;

/**
 * @author toufik youssef
 *
 */
public class GenericApiResponseDto {
	
	private Boolean status;
	private String message;
	private AccountDto accountDto;
	
	public GenericApiResponseDto() {
		super();
	}

	public GenericApiResponseDto(boolean status, String message) {
		super();
		this.status = status;
		this.message = message;
	}
	
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	public AccountDto getAccountDto() {
		return accountDto;
	}

	public void setAccountDto(AccountDto accountDto) {
		this.accountDto = accountDto;
	}
	

}
