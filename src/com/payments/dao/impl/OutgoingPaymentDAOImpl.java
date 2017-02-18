package com.payments.dao.impl;

import com.payments.dao.api.OutgoingPaymentDAO;
import com.payments.exception.DaoException;
import com.payments.model.OutgoingPayment;
import com.payments.model.enums.PaymentStatus;
import com.payments.utilites.Constants;
import com.payments.utilites.SqlQueries;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Kostiantyn Dubovik
 */
public class OutgoingPaymentDAOImpl extends DAOImpl<OutgoingPayment> implements OutgoingPaymentDAO {

    private static final BillDAOImpl BILL_DAO = BillDAOImpl.getInstance();
    private static final String FROM_BILL_ID = "from_bill_id";
    private static final String DATE = "date";
    private static final String TO_BILL = "to_bill";
    private static final String SUM = "sum";
    private static final String COMMISSION = "commission";
    private static final String COMMENT = "comment";
    private static final String PAYMENT_STATUS = "payment_status";
    private static volatile OutgoingPaymentDAOImpl instance;

    private static final Logger LOG = Logger.getLogger(UserDAOImpl.class);

    private OutgoingPaymentDAOImpl() {
        LOG.debug(String.format(Constants.ENTITY_WAS_CREATED, this.getClass().getSimpleName()));
    }

    /**
     * Method for get instance of {@link OutgoingPaymentDAOImpl}
     *
     * @return instance of {@link OutgoingPaymentDAOImpl}
     */
    public static OutgoingPaymentDAOImpl getInstance() {
        if (instance == null) {
            synchronized (OutgoingPaymentDAOImpl.class) {
                if (instance == null) {
                    instance = new OutgoingPaymentDAOImpl();
                }
            }
        }
        return instance;
    }

    @Override
    protected List<OutgoingPayment> readObjectFromResultSet(ResultSet resultSet, Connection connection) {
        List<OutgoingPayment> result = new LinkedList<>();
        try {
            while (resultSet.next()) {
                OutgoingPayment outgoingPayment = new OutgoingPayment();
                outgoingPayment.setId(resultSet.getInt(1));
                outgoingPayment.setPaymentFromBill(BILL_DAO.get(resultSet.getInt(FROM_BILL_ID), connection));
                outgoingPayment.setDateOfPayment(resultSet.getDate(DATE));
                outgoingPayment.setNumberOfBillWherePaymentGoing(resultSet.getString(TO_BILL));
                outgoingPayment.setSumOfPayment(resultSet.getBigDecimal(SUM));
                outgoingPayment.setCommission(resultSet.getBigDecimal(COMMISSION));
                outgoingPayment.setComment(resultSet.getString(COMMENT));
                outgoingPayment.setPaymentStatus(PaymentStatus.chooseStatus(resultSet.getInt(PAYMENT_STATUS)));
                result.add(outgoingPayment);
            }
        } catch (SQLException e) {
            LOG.error(e.getMessage());
            throw new DaoException(e);
        }
        return result;
    }

    @Override
    protected PreparedStatement createGetStatement(Connection connection, int id) {
        PreparedStatement ps;
        try {
            ps = connection.prepareStatement(SqlQueries.SELECT_OUTGOING_PAYMENT_BY_ID);
            ps.setInt(1, id);
        } catch (SQLException e) {
            LOG.error(e.getMessage());
            throw new DaoException(e);
        }
        return ps;
    }

    @Override
    protected String createGetAllStatement(String orderBy) {
        return String.format(SqlQueries.SELECT_ALL, "payments_outgoing_payments", orderBy);

    }

    @Override
    protected PreparedStatement createGetAllByIdStatement(int id, String orderBy, Connection connection) {
        PreparedStatement ps;
        try {
            ps = connection.prepareStatement(String.format(SqlQueries.SELECT_OUTGOING_PAYMENT_BY_USER_ID, orderBy));
            ps.setInt(1, id);
        } catch (SQLException e) {
            LOG.error(e.getMessage());
            throw new DaoException(e);
        }
        return ps;
    }

    @Override
    protected PreparedStatement createInsertStatement(Connection connection, OutgoingPayment entity) {
        PreparedStatement ps;
        try {
            ps = connection.prepareStatement(SqlQueries.INSERT_OUTGOING_PAYMENT, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setInt(1, entity.getBillWhereFromIsPayment().getId());
            ps.setString(2, entity.getNumberOfBillWherePaymentGoing());
            ps.setString(3, entity.getComment());
            ps.setDate(4, new Date(System.currentTimeMillis()));
            ps.setBigDecimal(5, entity.getSumOfPayment());
            ps.setBigDecimal(6, entity.getCommission());
        } catch (SQLException e) {
            LOG.error(e.getMessage());
            throw new DaoException(e);
        }
        return ps;
    }

    @Override
    protected PreparedStatement createUpdateStatement(Connection connection, OutgoingPayment outgoingPayment) {
        PreparedStatement ps;
        try {
            ps = connection.prepareStatement(String.format(SqlQueries.UPDATE, "payments_outgoing_payments", "date=?,payment_status=?"));
            ps.setDate(1, new Date(System.currentTimeMillis()));
            ps.setInt(2, outgoingPayment.getPaymentStatus().getId());
            ps.setInt(3, outgoingPayment.getId());
        } catch (SQLException e) {
            LOG.error(e.getMessage());
            throw new DaoException(e);
        }
        return ps;
    }

    @Override
    public int getCountOfRecords(int userId, Connection connection) {

        int result = 0;
        try {
            PreparedStatement ps = connection.prepareStatement(SqlQueries.SELECT_COUNT_OF_OUTGOING_PAYMENTS);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                result = rs.getInt(1);
            }
            releaseResources(rs, LOG, ps);
        } catch (SQLException e) {
            LOG.error(e.getMessage());
            throw new DaoException(e);
        }
        return result;
    }

    public OutgoingPayment getOutgoingPaymentByBillNumbers(String numberOfBillFrom, String numberOfBillTo, Connection connection) {
        OutgoingPayment result;
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement(SqlQueries.SELECT_OUTGOING_PAYMENT_BY_BILL_NUMBERS);
            preparedStatement.setString(1, numberOfBillTo);
            preparedStatement.setString(2, numberOfBillFrom);
            ResultSet resultSet = preparedStatement.executeQuery();
            result = readObjectFromResultSet(resultSet, connection).get(0);
            releaseResources(resultSet, LOG, preparedStatement);
        } catch (SQLException e) {
            LOG.error(e.getMessage());
            throw new DaoException(e);
        }
        return result;
    }
}
