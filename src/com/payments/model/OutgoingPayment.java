package com.payments.model;

public class OutgoingPayment extends Payment {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1024623164761900610L;

	private Bill paymentFromBill;

    private String numberOfBillWherePaymentGoing;


    public String getNumberOfBillWherePaymentGoing() {
        return numberOfBillWherePaymentGoing;
    }

    public void setNumberOfBillWherePaymentGoing(String numberOfBillWherePaymentGoing) {
        this.numberOfBillWherePaymentGoing = numberOfBillWherePaymentGoing;
    }

    public Bill getBillWhereFromIsPayment() {

        return paymentFromBill;
    }

    public void setPaymentFromBill(Bill paymentFromBill) {
        this.paymentFromBill = paymentFromBill;
    }

}
