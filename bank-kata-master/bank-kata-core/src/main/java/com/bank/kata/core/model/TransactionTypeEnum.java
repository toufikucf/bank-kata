package com.bank.kata.core.model;

/**
 * @author toufik youssef
 *
 */
public enum TransactionTypeEnum {
	
	WITHDRAW("withdraw"),
	DEPOSIT("deposit"),
	CREDIT("credit"),
	DEBIT("debit");
	
	private String value;
	
	TransactionTypeEnum(String value){
		this.value=value;
	}

	public String getValue() {
		return value;
	}
	
	@Override
	public String toString(){
		return this.value;
	}
}
