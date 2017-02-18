package com.payments.model.enums;

/**
 * @author Dubovik Kostiantyn
 */
public enum PaymentStatus {
    PREPARED(1,"payment.message.status.prepared"), SENT(2,"payment.message.status.sent");

    private final int id;
    private final String stringValue;

    PaymentStatus(int id, String stringValue) {
        this.id = id;
        this.stringValue = stringValue;
    }

    public int getId() {
        return id;
    }

    public String getStringValue (){
        return stringValue;
    }

    public static PaymentStatus chooseStatus(int id) {
        PaymentStatus result = PREPARED;
        for (PaymentStatus paymentStatus : values()) {
            if (paymentStatus.id == id) {
                result = paymentStatus;
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
