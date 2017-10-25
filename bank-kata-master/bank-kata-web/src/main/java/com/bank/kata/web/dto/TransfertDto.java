package com.bank.kata.web.dto;

/**
 * @author toufik youssef
 *
 */
public class TransfertDto {

	private String payerId;
	private String payeeId;
	private Double amount;

	public TransfertDto(String payerId, String payeeId, Double amount) {
		super();
		this.payerId = payerId;
		this.payeeId = payeeId;
		this.amount = amount;
	}
	public TransfertDto() {
		super();
	}

	public String getPayerId() {
		return payerId;
	}
	public void setPayerId(String payerId) {
		this.payerId = payerId;
	}
	public String getPayeeId() {
		return payeeId;
	}
	public void setPayeeId(String payeeId) {
		this.payeeId = payeeId;
	}
	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	@Override
	public String toString(){
		return this.payerId+" "+this.payeeId+" "+this.amount;
	}
}
