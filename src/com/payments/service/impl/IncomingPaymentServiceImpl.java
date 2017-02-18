package com.payments.service.impl;

import com.payments.datasource.DataSource;
import com.payments.exception.DaoException;
import com.payments.model.Bill;
import com.payments.model.IncomingPayment;
import com.payments.model.enums.PaymentStatus;
import com.payments.service.api.IncomingPaymentService;
import com.payments.utilites.Constants;
import com.payments.utilites.MailHelper;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;
import java.util.List;

/**
 * @author Kostiantyn Dubovik
 */
public class IncomingPaymentServiceImpl extends PaymentService<IncomingPayment> implements IncomingPaymentService {

    private static final Logger LOG = Logger.getLogger(IncomingPaymentServiceImpl.class);

    private static final DataSource DATA_SOURCE = DataSource.getInstance();


    private static volatile IncomingPaymentServiceImpl instance;

    public static IncomingPaymentServiceImpl getInstance() {
        if (instance == null) {
            synchronized (IncomingPaymentServiceImpl.class) {
                if (instance == null) {
                    instance = new IncomingPaymentServiceImpl();
                }
            }
        }
        return instance;
    }

    private IncomingPaymentServiceImpl() {
        LOG.debug(String.format(Constants.ENTITY_WAS_CREATED, this.getClass().getSimpleName()));
    }

    @Override
    public void insert(IncomingPayment incomingPayment) {
        Connection connection = DATA_SOURCE.getConnection();
        String[] languageAndEmail = USER_DAO.getLanguageAndEmail(incomingPayment.getPaymentToBill().getUserId(), connection);
        try {
            INCOMING_PAYMENT_DAO.insert(incomingPayment, connection);
            transferFounds(incomingPayment, incomingPayment.getPaymentToBill(), connection);
            Bill adminBill = BILL_DAO.getBillByNumber(ADMIN_BILL_NUMBER, connection);
            IncomingPayment commissionPayment = createCommissionPayment(incomingPayment, adminBill, connection);
            MailHelper.sendMailWithPdfReport(transferFounds(commissionPayment, adminBill, connection),ADMIN_LANG,ADMIN_MAIL);
            commit(connection, LOG);
            MailHelper.sendMailWithPdfReport(incomingPayment, languageAndEmail[0], languageAndEmail[1]);
        } catch (DaoException ex) {
            rollback(connection, LOG);
        } finally {
            close(connection, LOG);
        }
    }

    @Override
    public List<IncomingPayment> getAll(int id, int startPosition, String orderBy) {
        Connection connection = DATA_SOURCE.getConnection();
        List<IncomingPayment> result = null;
        try {
            result = INCOMING_PAYMENT_DAO.getAllById(id, startPosition, orderBy, connection);
        } catch (DaoException ex) {
            rollback(connection, LOG);
        } finally {
            close(connection, LOG);
        }
        return result;
    }

    @Override
    public List<IncomingPayment> getAll(int startPosition, String orderBy) {
        Connection connection = DATA_SOURCE.getConnection();
        List<IncomingPayment> result = null;
        try {
            result = INCOMING_PAYMENT_DAO.getAllById(startPosition, orderBy, connection);
        } catch (DaoException ex) {
            rollback(connection, LOG);
        } finally {
            close(connection, LOG);
        }
        return result;
    }


    public int getCountOfRecords(int userId) {
        Connection connection = DATA_SOURCE.getConnection();
        int result = 0;
        try {
            result = INCOMING_PAYMENT_DAO.getCountOfRecords(userId, connection);
        } catch (DaoException ex) {
            rollback(connection, LOG);
        } finally {
            close(connection, LOG);
        }
        return result;
    }


    @Override
    public void update(IncomingPayment entity) {
        Connection connection = DATA_SOURCE.getConnection();
        try {
            INCOMING_PAYMENT_DAO.update(entity, connection);
            commit(connection,LOG);
        } catch (DaoException e) {
            rollback(connection, LOG);
        } finally {
            close(connection, LOG);
        }
    }

    @Override
    public IncomingPayment getById(int id) {
        Connection connection = DATA_SOURCE.getConnection();
        IncomingPayment incomingPayment;
        try {
            incomingPayment = INCOMING_PAYMENT_DAO.get(id, connection);
        } finally {
            close(connection, LOG);
        }
        return incomingPayment;
    }

    @Override
    public IncomingPayment getIncomingPaymentByBillNumbers(String numberOfBillFrom, String numberOfBillTo) {
        Connection connection = DATA_SOURCE.getConnection();
        IncomingPayment incomingPayment = null;
        try {
            incomingPayment = INCOMING_PAYMENT_DAO.getIncomingPaymentByBillNumbers(numberOfBillFrom, numberOfBillTo, connection);
        } catch (DaoException e) {
            rollback(connection, LOG);
        } finally {
            close(connection, LOG);
        }

        return incomingPayment;
    }

    private IncomingPayment createCommissionPayment(IncomingPayment incomingPayment, Bill adminBill, Connection connection) {
        IncomingPayment commissionPayment  = new IncomingPayment();
        commissionPayment.setPaymentStatus(PaymentStatus.SENT);
        commissionPayment.setPaymentToBill(adminBill);
        commissionPayment.setSumOfPayment(incomingPayment.getCommission());
        commissionPayment.setPaymentFromBill(incomingPayment.getPaymentToBill().getBillNumber());
        commissionPayment.setComment(String.format(COMMENT,"",incomingPayment.getPaymentToBill().getBillNumber()));
        commissionPayment.setCommission(new BigDecimal("0"));
        commissionPayment.setDateOfPayment(new Date(System.currentTimeMillis()));
        return commissionPayment;
    }
}
