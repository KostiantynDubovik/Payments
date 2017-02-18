package com.payments.dao.api;

import com.payments.model.IncomingPayment;

import java.sql.Connection;

/**
 * @author Kostiantyn Dubovik
 */
public interface IncomingPaymentDAO extends DAO <IncomingPayment> {
    IncomingPayment getIncomingPaymentByBillNumbers(String numberOfBillFrom, String numberOfBillTo, Connection connection);
}
