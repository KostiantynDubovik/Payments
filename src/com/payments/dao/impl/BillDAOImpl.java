package com.payments.dao.impl;

import com.payments.dao.api.BillDAO;
import com.payments.exception.DaoException;
import com.payments.model.Bill;
import com.payments.utilites.Constants;
import com.payments.utilites.SqlQueries;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Kostiantyn Dubovik
 */
public class BillDAOImpl extends DAOImpl<Bill> implements BillDAO {

    private static final String ID = "id";
    private static final String USER_ID = "user_id";
    private static final String NUMBER = "number";
    private static final String NAME = "name";
    private static final String BALANCE = "balance";
    private static final String BLOCKED = "blocked";
    private static final String NEED_TO_UNBLOCK = "need_to_unblock";
    private static final String PAYMENTS_BILLS = "payments_bills";
    private static final String USER_ID_USER_ID = "user_id=user_id";
    private static final String USER_ID_PS = "user_id=?";

    private static volatile BillDAOImpl instance;

    private static final Logger LOG = Logger.getLogger(BillDAOImpl.class);

    private BillDAOImpl() {
        LOG.debug(String.format(Constants.ENTITY_WAS_CREATED, this.getClass().getSimpleName()));
    }

    /**
     * Method for get instance of {@link BillDAOImpl}
     *
     * @return instance of {@link BillDAOImpl}
     */
    public static BillDAOImpl getInstance() {
        if (instance == null) {
            synchronized (BillDAOImpl.class) {
                if (instance == null) {
                    instance = new BillDAOImpl();
                }
            }
        }
        return instance;
    }

    @Override
    protected List<Bill> readObjectFromResultSet(ResultSet resultSet, Connection connection) {
        List<Bill> bills = new LinkedList<>();
        Bill bill;
        try {
            while (resultSet.next()) {
                bill = new Bill();
                bill.setId(resultSet.getInt(ID));
                bill.setUserId(resultSet.getInt(USER_ID));
                bill.setBillNumber(resultSet.getString(NUMBER));
                bill.setBillName(resultSet.getString(NAME));
                bill.setBalance(resultSet.getBigDecimal(BALANCE).setScale(2, BigDecimal.ROUND_HALF_UP));
                bill.setBlocked(resultSet.getBoolean(BLOCKED));
                bill.setNeedToUnblock(resultSet.getBoolean(NEED_TO_UNBLOCK));
                bills.add(bill);
            }
        } catch (SQLException e) {
            LOG.error(e.getMessage());
            throw new DaoException(e);
        }
        return bills;
    }

    @Override
    protected PreparedStatement createGetStatement(Connection connection, int billId) {
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement(SqlQueries.SELECT_BILL);
            preparedStatement.setInt(1, billId);
        } catch (SQLException e) {
            LOG.error(e.getMessage());
            throw new DaoException(e);
        }
        return preparedStatement;
    }

    @Override
    protected String createGetAllStatement(String orderBy) {
        return String.format(SqlQueries.SELECT_ALL, PAYMENTS_BILLS, orderBy);
    }

    @Override
    protected PreparedStatement createGetAllByIdStatement(int userId, String orderBy, Connection connection) {
        PreparedStatement preparedStatement;
        try {
            if (userId == 0) {
                preparedStatement = connection.prepareStatement(String.format(SqlQueries.SELECT_BILLS_BY_USER_ID, USER_ID_USER_ID, orderBy));
            } else {
                preparedStatement = connection.prepareStatement(String.format(SqlQueries.SELECT_BILLS_BY_USER_ID, USER_ID_PS, orderBy));
                preparedStatement.setInt(1, userId);
            }
        } catch (SQLException e) {
            LOG.error(e.getMessage());
            throw new DaoException(e);
        }
        return preparedStatement;
    }

    @Override
    protected PreparedStatement createInsertStatement(Connection connection, Bill entity) {
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement(SqlQueries.INSERT_BILL, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, entity.getUserId());
            preparedStatement.setString(2, entity.getBillNumber());
            preparedStatement.setString(3, entity.getBillName());
            preparedStatement.setBigDecimal(4, entity.getBalance());
        } catch (SQLException e) {
            LOG.error(e.getMessage());
            throw new DaoException(e);
        }
        return preparedStatement;
    }


    @Override
    protected PreparedStatement createUpdateStatement(Connection connection, Bill bill) {

        PreparedStatement ps;
        try {
            ps = connection.prepareStatement(SqlQueries.UPDATE_BILL);
            ps.setBigDecimal(1, bill.getBalance());
            ps.setBoolean(2, bill.isBlocked());
            ps.setBoolean(3, bill.isNeedToUnblock());
            ps.setInt(4, bill.getId());
        } catch (SQLException e) {
            LOG.error(e.getMessage());
            throw new DaoException(e);
        }
        return ps;
    }

    @Override
    public Bill getBillByNumber(String billNumber, Connection connection) {
        Bill bill;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SqlQueries.GET_BILL_BY_NUMBER);
            preparedStatement.setString(1, billNumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            bill = readObjectFromResultSet(resultSet, connection).get(0);
            releaseResources(resultSet, LOG, preparedStatement);
        } catch (SQLException e) {
            LOG.error(e.getMessage());
            throw new DaoException(e);
        } catch (IndexOutOfBoundsException ex){
            bill = null;
        }
        return bill;
    }

    /**
     * @param userId of needed user
     * @return count of needed bills
     */
    @Override
    public synchronized int getCountOfRecords(int userId, Connection connection) {
        int countOfRecords = 0;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SqlQueries.SELECT_COUNT_OF_BILLS);
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                countOfRecords = resultSet.getInt(1);
            }
            releaseResources(resultSet, LOG, preparedStatement);
        } catch (SQLException e) {
            LOG.error(e.getMessage());
            throw new DaoException(e);
        }
        return countOfRecords;
    }

}
