package com.payments.utilites;

import com.payments.dto.PaymentBean;
import com.payments.model.Bill;


import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Kostiantyn Dubovik
 */
public class ValidatePayment {

    private final static int ROUNDING_MODE = BigDecimal.ROUND_HALF_UP;

    private final static BigDecimal DIVIDER = new BigDecimal("25").setScale(2, ROUNDING_MODE);

    private final static BigDecimal MIN_COMMISSION = new BigDecimal("5").setScale(2, ROUNDING_MODE);
    private final static BigDecimal MAX_COMMISSION = new BigDecimal("1000").setScale(2, ROUNDING_MODE);


    public static BigDecimal getCommission(BigDecimal sum) {
        BigDecimal commission = sum.divide(DIVIDER, ROUNDING_MODE).setScale(2, ROUNDING_MODE);
        if (commission.compareTo(MIN_COMMISSION) == -1) {
            commission = MIN_COMMISSION;
        } else if (commission.compareTo(MAX_COMMISSION) == 1) {
            commission = MAX_COMMISSION;
        }
        return commission;
    }

    public static Map<String, String> validatePayment(PaymentBean paymentBean) {
        Map<String, String> errorMessages = new HashMap<>();
        validateBillFromNumber(errorMessages, paymentBean);
        validateBillToNumber(errorMessages, paymentBean);
        if (paymentBean.isOutgoing()) {
            validateOwnerForOut(errorMessages, paymentBean);
        } else {
            validateOwnerForIn(errorMessages, paymentBean);
        }
        if (!errorMessages.containsKey("user_is_not_owner_of_bill")) {
            validateForBlocked(errorMessages, paymentBean);
            validateComment(errorMessages, paymentBean);
            validateBalance(errorMessages, paymentBean);
        }

        return errorMessages;
    }

    public static Map<String, String> validateRefill(PaymentBean paymentBean) {
        Map<String, String> errorMessages = new HashMap<>();
        validateBillToNumber(errorMessages, paymentBean);
        validateOwnerForIn(errorMessages, paymentBean);
        if (!errorMessages.containsKey("user_is_not_owner_of_bill")) {
            validateForBlocked(errorMessages, paymentBean);
        }
        return errorMessages;
    }


    private static void validateBalance(Map<String, String> errorMessages, PaymentBean paymentBean) {
        if (paymentBean.isOutgoing() && paymentBean.getBill().getBalance().compareTo(paymentBean.getSumOfPayment().add(paymentBean.getCommission())) < 0) {
            errorMessages.put("not_enough_money", "payment.message.not_enough_money");
        }
    }

    private static void validateComment(Map<String, String> errorMessages, PaymentBean paymentBean) {
        if (!Patterns.COMMENT_LENGTH.matcher(paymentBean.getComment()).find()) {
            errorMessages.put("to_long_comment", "message.to_long_comment");
        }
    }

    private static void validateBillFromNumber(Map<String, String> errorMessages, PaymentBean paymentBean) {
        String billFrom = paymentBean.getBillFromNumber();
        if (!Patterns.BILL_NUMBER.matcher(billFrom).find() || billFrom.isEmpty()) {
            errorMessages.put("invalid_bill_from_number", "message.invalid_bill_number");
        }
    }

    private static void validateBillToNumber(Map<String, String> errorMessages, PaymentBean paymentBean) {
        String billTo = paymentBean.getBillToNumber();
        if (!Patterns.BILL_NUMBER.matcher(billTo).find() || billTo.isEmpty()) {
            errorMessages.put("invalid_bill_to_number", "message.invalid_bill_number");
        }
    }

    private static void validateOwnerForIn(Map<String, String> errorMessages, PaymentBean paymentBean) {
        for (Bill userBill : paymentBean.getUserBills()) {
            if (userBill.getBillNumber().equals(paymentBean.getBillToNumber())) {
                paymentBean.setBill(userBill);
                System.err.println(userBill.getUserId());
                return;
            }
        }
        errorMessages.put("user_is_not_owner_of_bill", "message.user_is_not_owner_of_bill");
    }

    private static void validateOwnerForOut(Map<String, String> errorMessages, PaymentBean paymentBean) {
        for (Bill userBill : paymentBean.getUserBills()) {
            System.err.println(userBill.getBillNumber());
            System.err.println(paymentBean.getBillFromNumber());
            if (userBill.getBillNumber().equals(paymentBean.getBillFromNumber())) {
                paymentBean.setBill(userBill);
                System.err.println(userBill.getUserId());
                return;
            }
        }
        errorMessages.put("user_is_not_owner_of_bill", "message.user_is_not_owner_of_bill");
    }

    private static void validateForBlocked(Map<String, String> errorMessages, PaymentBean paymentBean) {
        if (paymentBean.getBill().isBlocked()) {
            errorMessages.put("bill_is_blocked", "message.bill_is_blocked");
        }
    }
}
