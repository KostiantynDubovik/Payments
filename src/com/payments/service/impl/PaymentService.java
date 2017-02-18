package com.payments.service.impl;

import com.payments.dao.api.BillDAO;
import com.payments.dao.api.IncomingPaymentDAO;
import com.payments.dao.api.UserDAO;
import com.payments.dao.impl.BillDAOImpl;
import com.payments.dao.impl.IncomingPaymentDAOImpl;
import com.payments.dao.impl.UserDAOImpl;
import com.payments.datasource.DataSource;
import com.payments.model.Bill;
import com.payments.model.IncomingPayment;
import com.payments.model.OutgoingPayment;
import com.payments.model.Payment;
import com.payments.model.enums.PaymentStatus;
import com.payments.service.api.Service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;
import java.util.List;

/**
 * @author Kostiantyn Dubovik
 */
public abstract class PaymentService<P extends Payment> implements Service<P> {


    static final IncomingPaymentDAO INCOMING_PAYMENT_DAO = IncomingPaymentDAOImpl.getInstance();
    static final BillDAO BILL_DAO = BillDAOImpl.getInstance();
    static final UserDAO USER_DAO = UserDAOImpl.getInstance();
    static final DataSource DATA_SOURCE = DataSource.getInstance();


    static final String COMMENT = "Commission for transferring founds from %s to %s";
    static final String ADMIN_BILL_NUMBER = "5662118945133203";
    static final String ADMIN_MAIL = "mail.for.my.shop@gmail.com";
    static final String ADMIN_LANG = "ru_RU";




    Bill getBillForTransfer(String billNumber, Connection con) {
        return BILL_DAO.getBillByNumber(billNumber, con);
    }

    IncomingPayment transferFounds(Payment payment, Bill bill, Connection connection) {

        IncomingPayment incomingPayment;
        if (payment instanceof OutgoingPayment) {
            incomingPayment = createIncomingPayment((OutgoingPayment) payment, bill);
        } else {
            incomingPayment = (IncomingPayment) payment;
        }
        INCOMING_PAYMENT_DAO.insert(incomingPayment, connection);
        incomingPayment.setDateOfPayment(new Date(System.currentTimeMillis()));
        incomingPayment.setPaymentStatus(PaymentStatus.SENT);
        bill.setBalance(bill.getBalance().add(incomingPayment.getSumOfPayment()));
        BILL_DAO.update(bill, connection);
        return incomingPayment;
    }

    private IncomingPayment createIncomingPayment(OutgoingPayment outgoingPayment, Bill bill) {
        IncomingPayment incomingPayment = new IncomingPayment();
        incomingPayment.setSumOfPayment(outgoingPayment.getSumOfPayment());
        incomingPayment.setPaymentFromBill(outgoingPayment.getBillWhereFromIsPayment().getBillNumber());
        incomingPayment.setPaymentToBill(bill);
        incomingPayment.setComment(outgoingPayment.getComment());
        incomingPayment.setCommission(new BigDecimal("0"));
        return incomingPayment;
    }

    public abstract List<P> getAll(int startPosition, String orderBy);
}
