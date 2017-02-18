package com.payments.service.impl;

import com.payments.dao.api.OutgoingPaymentDAO;
import com.payments.dao.impl.OutgoingPaymentDAOImpl;
import com.payments.exception.BillBlockedException;
import com.payments.exception.DaoException;
import com.payments.model.Bill;
import com.payments.model.IncomingPayment;
import com.payments.model.OutgoingPayment;
import com.payments.model.enums.PaymentStatus;
import com.payments.service.api.OutgoingPaymentService;
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
public class OutgoingPaymentServiceImpl extends PaymentService<OutgoingPayment> implements OutgoingPaymentService {

	private static final Logger LOG = Logger.getLogger(OutgoingPaymentServiceImpl.class);
	private static final BigDecimal COMMISSION = new BigDecimal("0");

	private static volatile OutgoingPaymentServiceImpl instance;

	private static final OutgoingPaymentDAO OUTGOING_PAYMENT_DAO = OutgoingPaymentDAOImpl.getInstance();

	public static OutgoingPaymentServiceImpl getInstance() {
		if (instance == null) {
			synchronized (OutgoingPaymentServiceImpl.class) {
				if (instance == null) {
					instance = new OutgoingPaymentServiceImpl();
				}
			}
		}
		return instance;
	}

	private OutgoingPaymentServiceImpl() {
		LOG.debug(String.format(Constants.ENTITY_WAS_CREATED, this.getClass().getSimpleName()));
	}

	public void insert(OutgoingPayment outgoingPayment) {
		Connection connection = DATA_SOURCE.getConnection();
		try {
			OUTGOING_PAYMENT_DAO.insert(outgoingPayment, connection);
			commit(connection,LOG);
		} catch (DaoException ex) {
			rollback(connection, LOG);
		} finally {
			close(connection, LOG);
		}
	}

	public void update(OutgoingPayment outgoingPayment) {
		Connection connection = DATA_SOURCE.getConnection();
		try {
			OUTGOING_PAYMENT_DAO.update(outgoingPayment, connection);
			Bill billFrom = outgoingPayment.getBillWhereFromIsPayment();
			setBalance(billFrom,outgoingPayment);
			BILL_DAO.update(billFrom, connection);
			String[] languageAndEmailOfSender = USER_DAO.getLanguageAndEmail(billFrom.getUserId(), connection);
			IncomingPayment commissionPayment = createCommissionTransfer(outgoingPayment, connection);
			commit(connection, LOG);
			MailHelper.sendMailWithPdfReport(commissionPayment,ADMIN_LANG,ADMIN_MAIL);
			Bill billTo = getBillForTransfer(outgoingPayment.getNumberOfBillWherePaymentGoing(), connection);

			if (billTo != null && !billTo.isBlocked()) {
				BILL_DAO.update(billFrom, connection);
				IncomingPayment incomingPayment = transferFounds(outgoingPayment, billTo, connection);
				commit(connection, LOG);
				String[] languageAndEmailOfRecipient = USER_DAO.getLanguageAndEmail(billTo.getUserId(), connection);
				MailHelper.sendMailWithPdfReport(incomingPayment, languageAndEmailOfRecipient[0], languageAndEmailOfRecipient[1]);
			} else if (billTo != null && billTo.isBlocked()) {
				rollback(connection, LOG);
				throw new BillBlockedException(billTo.getBillNumber());
			} else {
				commit(connection, LOG);
				MailHelper.sendMailWithPdfReport(outgoingPayment, languageAndEmailOfSender[0], languageAndEmailOfSender[1]);
			}
		} catch (DaoException e) {
			LOG.error(e.getMessage());
			rollback(connection, LOG);
		} finally {
			close(connection, LOG);
		}
	}

	@Override
	public OutgoingPayment getById(int id) {
		Connection connection = DATA_SOURCE.getConnection();
		OutgoingPayment outgoingPayment;
		try {
			outgoingPayment = OUTGOING_PAYMENT_DAO.get(id, connection);
		} finally {
			close(connection, LOG);
		}
		return outgoingPayment;
	}

	private IncomingPayment createCommissionTransfer(OutgoingPayment outgoingPayment, Connection connection) {
		OutgoingPayment commissionPayment = new OutgoingPayment();
		commissionPayment.setComment(String.format(COMMENT,outgoingPayment.getBillWhereFromIsPayment().getBillNumber(),outgoingPayment.getNumberOfBillWherePaymentGoing()));
		commissionPayment.setPaymentFromBill(outgoingPayment.getBillWhereFromIsPayment());
		commissionPayment.setNumberOfBillWherePaymentGoing(ADMIN_BILL_NUMBER);
		commissionPayment.setSumOfPayment(outgoingPayment.getCommission());
		commissionPayment.setCommission(COMMISSION);
		commissionPayment.setPaymentStatus(PaymentStatus.SENT);
		commissionPayment.setDateOfPayment(new Date(System.currentTimeMillis()));
		Bill adminBill =  BILL_DAO.getBillByNumber(ADMIN_BILL_NUMBER, connection);
		IncomingPayment incomingPayment = transferFounds(commissionPayment,adminBill, connection);
		return incomingPayment;
	}

	@Override
	public int getCountOfRecords(int userId) {
		Connection connection = DATA_SOURCE.getConnection();
		int result;
		try {
			result = OUTGOING_PAYMENT_DAO.getCountOfRecords(userId, connection);
		} finally {
			close(connection, LOG);
		}
		return result;
	}

	@Override
	public List<OutgoingPayment> getAll(int startPosition, String orderBy) {
		Connection connection = DATA_SOURCE.getConnection();
		List<OutgoingPayment> outgoingPayments;
		try {
			outgoingPayments = OUTGOING_PAYMENT_DAO.getAllById(startPosition, orderBy, connection);
		} finally {
			close(connection, LOG);
		}
		return outgoingPayments;
	}

	@Override
	public List<OutgoingPayment> getAll(int id, int startPosition, String orderBy) {
		Connection connection = DATA_SOURCE.getConnection();
		List<OutgoingPayment> outgoingPayments;
		try {
			outgoingPayments = OUTGOING_PAYMENT_DAO.getAllById(id, startPosition, orderBy, connection);
		} finally {
			close(connection, LOG);
		}
		return outgoingPayments;
	}

	private void setBalance (Bill billFrom, OutgoingPayment outgoingPayment){
		billFrom.setBalance(outgoingPayment.getBillWhereFromIsPayment().getBalance()
				.subtract(outgoingPayment.getSumOfPayment().add(outgoingPayment.getCommission())));
	}

	@Override
	public OutgoingPayment getOutgoingPaymentByBillNumbers(String numberOfBillFrom, String numberOfBillTo) {
		OutgoingPayment result;
		Connection connection = DATA_SOURCE.getConnection();
		try {
			result = OUTGOING_PAYMENT_DAO.getOutgoingPaymentByBillNumbers(numberOfBillFrom, numberOfBillTo, connection);
		} finally {
			close(connection, LOG);
		}
		return result;
	}
}
