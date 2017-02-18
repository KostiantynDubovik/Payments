package com.payments.model;

public class IncomingPayment extends Payment {

    /**
	 * 
	 */
	private static final long serialVersionUID = -1764595010954379423L;

	private String paymentFromBill;

    private Bill paymentToBill;

    public Bill getPaymentToBill() {
        return paymentToBill;
    }

    public void setPaymentToBill(Bill paymentToBill) {
        this.paymentToBill = paymentToBill;
    }

    public String getPaymentFromBill() {
        return paymentFromBill;
    }

    public void setPaymentFromBill(String paymentFromBill) {
        this.paymentFromBill = paymentFromBill;
    }
}
