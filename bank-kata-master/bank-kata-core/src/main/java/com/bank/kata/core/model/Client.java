package com.bank.kata.core.model;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author toufik youssef
 *
 */
@Entity
public class Client {

	private String clientId;
	private String name;
	private String pwd; 

	public Client(String clientId, String name, String pwd) {
		super();
		this.clientId = clientId;
		this.name = name;
		this.pwd = pwd;
	}

	public Client() {
		super();
	}

	@Id
	public String getClientId() {
		return this.clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	

}