package com.payments.service.api;

import com.payments.model.OutgoingPayment;

/**
 * @author Kostiantyn Dubovik
 */
public interface OutgoingPaymentService extends Service<OutgoingPayment>{
    OutgoingPayment getOutgoingPaymentByBillNumbers(String numberOfBillFrom, String numberOfBillTo);
}
