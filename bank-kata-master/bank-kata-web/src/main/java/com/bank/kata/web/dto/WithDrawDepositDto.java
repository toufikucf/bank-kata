package com.bank.kata.web.dto;

/**
 * @author toufik youssef
 *
 */
public class WithDrawDepositDto {

	private String accountId;
	private Double amount;

	public WithDrawDepositDto() {
		super();
	}

	public WithDrawDepositDto(String accountId, Double amount) {
		super();
		this.accountId = accountId;
		this.amount = amount;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	@Override
	public String toString(){
		return this.accountId+" "+this.amount;
	}
}
