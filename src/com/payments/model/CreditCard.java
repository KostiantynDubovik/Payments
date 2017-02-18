package com.payments.model;

import java.sql.Timestamp;

public class CreditCard extends Entity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2696343799491071192L;

	private int userId;

	private String numberOfCard;

	private Bill bill;

	private Timestamp expirationDate;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public Timestamp getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Timestamp expirationDate) {
		this.expirationDate = expirationDate;
	}

	public Bill getBill() {
		return bill;
	}

	public void setBill(Bill bill) {
		this.bill = bill;
	}

	public String getNumberOfCard() {
		return numberOfCard;
	}

	public void setNumberOfCard(String numberOfCard) {
		this.numberOfCard = numberOfCard;
	}
}
