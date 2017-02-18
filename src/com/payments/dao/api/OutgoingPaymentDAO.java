package com.payments.dao.api;

import com.payments.model.OutgoingPayment;

import java.sql.Connection;

/**
 * @author Kostiantyn Dubovik
 */
public interface OutgoingPaymentDAO extends DAO <OutgoingPayment> {
    OutgoingPayment getOutgoingPaymentByBillNumbers(String numberOfBillFrom, String numberOfBillTo, Connection connection);
}
