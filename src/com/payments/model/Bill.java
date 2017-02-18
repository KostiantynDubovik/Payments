package com.payments.model;

import java.math.BigDecimal;

public class Bill extends Entity {

    /**
     *
     */
    private static final long serialVersionUID = -4728459000634106616L;

    private int userId;

    private String billName;

    private String billNumber;

    private BigDecimal balance;

    private boolean blocked;

    private boolean needToUnblock;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getBillName() {
        if (billName == null) {
            billName = "";
        }
        return billName;
    }

    public void setBillName(String billName) {
        this.billName = billName;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public boolean isNeedToUnblock() {
        return needToUnblock;
    }

    public void setNeedToUnblock(boolean needToUnblock) {
        this.needToUnblock = needToUnblock;
    }
}
