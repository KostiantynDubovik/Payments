package com.payments.service.api;

import com.payments.model.IncomingPayment;

/**
 * @author Kostiantyn Dubovik
 */
public interface IncomingPaymentService extends Service<IncomingPayment>{
    IncomingPayment getIncomingPaymentByBillNumbers(String numberOfBillFrom, String numberOfBillTo);
}
