package com.payments.model;

import com.payments.model.enums.PaymentStatus;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Kostiantyn Dubovik
 */
public abstract class Payment extends Entity {

    /**
	 * 
	 */
	private static final long serialVersionUID = 7847819998173180367L;

	private PaymentStatus paymentStatus;

    private BigDecimal sumOfPayment;

    private Date dateOfPayment;

    private String comment;

    private BigDecimal commission;



    public BigDecimal getCommission() {
        return commission;
    }

    public void setCommission(BigDecimal commission) {
        this.commission = commission;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Date getDateOfPayment() {
        return dateOfPayment;
    }

    public void setDateOfPayment(Date dateOfPayment) {
        this.dateOfPayment = dateOfPayment;
    }

    public BigDecimal getSumOfPayment() {
        return sumOfPayment;
    }

    public void setSumOfPayment(BigDecimal sumOfPayment) {
        this.sumOfPayment = sumOfPayment;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
