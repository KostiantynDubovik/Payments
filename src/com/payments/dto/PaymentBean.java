package com.payments.dto;

import com.payments.model.Bill;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Kostiantyn Dubovik
 */
public class PaymentBean {

    private List<Bill> userBills;

    private String billFromNumber;

    private Bill bill;

    private String billToNumber;

    private BigDecimal sumOfPayment;

    private BigDecimal commission;

    private String comment;

    private boolean from;

    public List<Bill> getUserBills() {
        return userBills;
    }

    public void setUserBills(List<Bill> userBills) {
        this.userBills = userBills;
    }

    public String getBillFromNumber() {
        return billFromNumber;
    }

    public void setBillFromNumber(String billFromNumber) {
        this.billFromNumber = billFromNumber;
    }

    public String getBillToNumber() {
        return billToNumber;
    }

    public void setBillToNumber(String billToNumber) {
        this.billToNumber = billToNumber;
    }

    public BigDecimal getSumOfPayment() {
        return sumOfPayment;
    }

    public void setSumOfPayment(BigDecimal sumOfPayment) {
        this.sumOfPayment = sumOfPayment;
    }

    public BigDecimal getCommission() {
        return commission;
    }

    public void setCommission(BigDecimal commission) {
        this.commission = commission;
    }

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill billFrom) {
        this.bill = billFrom;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isOutgoing() {
        return from;
    }

    public void setFrom(boolean from) {
        this.from = from;
    }
}
