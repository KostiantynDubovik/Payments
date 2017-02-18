package com.payments.dao.impl;

import com.payments.dao.api.IncomingPaymentDAO;
import com.payments.exception.DaoException;
import com.payments.model.IncomingPayment;
import com.payments.utilites.Constants;
import com.payments.utilites.SqlQueries;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Kostiantyn Dubovik
 */
public class IncomingPaymentDAOImpl extends DAOImpl<IncomingPayment> implements IncomingPaymentDAO {

    private static final BillDAOImpl BILL_DAO = BillDAOImpl.getInstance();
    private static final String PAYMENTS_INCOMING_PAYMENTS = "payments_incoming_payments";
    private static final String STATUS = "status=?";
    private static volatile IncomingPaymentDAOImpl instance;

    private static final Logger LOG = Logger.getLogger(UserDAOImpl.class);

    private IncomingPaymentDAOImpl() {
        LOG.debug(String.format(Constants.ENTITY_WAS_CREATED, this.getClass().getSimpleName()));
    }

    /**
     * Method for get instance of {@link IncomingPaymentDAOImpl}
     *
     * @return instance of {@link IncomingPaymentDAOImpl}
     */
    public static IncomingPaymentDAOImpl getInstance() {
        if (instance == null) {
            synchronized (IncomingPaymentDAOImpl.class) {
                if (instance == null) {
                    instance = new IncomingPaymentDAOImpl();
                }
            }
        }
        return instance;
    }

    @Override
    protected List<IncomingPayment> readObjectFromResultSet(ResultSet resultSet, Connection connection) {
        List<IncomingPayment> result = new LinkedList<>();
        try {
            while (resultSet.next()) {
                IncomingPayment incomingPayment = new IncomingPayment();
                incomingPayment.setId(resultSet.getInt(1));
                incomingPayment.setPaymentFromBill(resultSet.getString("from_bill"));
                incomingPayment.setDateOfPayment(resultSet.getDate("date"));
                incomingPayment.setPaymentToBill(BILL_DAO.get(resultSet.getInt("to_bill_id"), connection));
                incomingPayment.setSumOfPayment(resultSet.getBigDecimal("sum").setScale(2, BigDecimal.ROUND_HALF_UP));
                incomingPayment.setComment(resultSet.getString("comment"));
                incomingPayment.setCommission(new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP));
                result.add(incomingPayment);
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
            ps = connection.prepareStatement(SqlQueries.SELECT_INCOMING_PAYMENT_BY_ID);
            ps.setInt(1, id);
        } catch (SQLException e) {
            LOG.error(e.getMessage());
            throw new DaoException(e);
        }
        return ps;
    }

    @Override
    protected String createGetAllStatement(String orderBy) {
        return String.format(SqlQueries.SELECT_ALL, PAYMENTS_INCOMING_PAYMENTS, orderBy);
    }

    @Override
    protected PreparedStatement createGetAllByIdStatement(int id, String orderBy, Connection connection) {
        PreparedStatement ps;
        try {
            ps = connection.prepareStatement(String.format(SqlQueries.SELECT_INCOMING_PAYMENT_BY_BILL_ID, orderBy));
            ps.setInt(1, id);
        } catch (SQLException e) {
            LOG.error(e.getMessage());
            throw new DaoException(e);
        }
        return ps;
    }

    @Override
    protected PreparedStatement createInsertStatement(Connection connection, IncomingPayment incomingPayment) {
        PreparedStatement ps;
        try {
            ps = connection.prepareStatement(SqlQueries.INSERT_INCOMING_PAYMENT, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, incomingPayment.getPaymentFromBill());
            ps.setInt(2, incomingPayment.getPaymentToBill().getId());
            ps.setString(3, incomingPayment.getComment());
            ps.setDate(4, new Date(System.currentTimeMillis()));
            ps.setBigDecimal(5, incomingPayment.getSumOfPayment());
            ps.setBigDecimal(6, incomingPayment.getCommission());
        } catch (SQLException e) {
            LOG.error(e.getMessage());
            throw new DaoException(e);
        }
        return ps;
    }

    @Override
    protected PreparedStatement createUpdateStatement(Connection connection, IncomingPayment incomingPayment) {
        PreparedStatement ps;
        try {
            ps = connection.prepareStatement(String.format(SqlQueries.UPDATE, PAYMENTS_INCOMING_PAYMENTS, STATUS));
            ps.setInt(1, incomingPayment.getPaymentStatus().getId());
        } catch (SQLException e) {
            LOG.error(e.getMessage());
            throw new DaoException(e);
        }
        return ps;
    }

    @Override
    public int getCountOfRecords(int id, Connection con) {
        int result = 0;
        try {
            PreparedStatement ps = con.prepareStatement(SqlQueries.SELECT_COUNT_OF_INCOMING_PAYMENTS);
            ps.setInt(1, id);
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

    public IncomingPayment getIncomingPaymentByBillNumbers(String numberOfBillFrom, String numberOfBillTo, Connection connection) {
        IncomingPayment result;
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement(SqlQueries.SELECT_INCOMING_PAYMENT_BY_BILL_NUMBERS);
            preparedStatement.setString(1, numberOfBillFrom);
            preparedStatement.setString(2, numberOfBillTo);
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
