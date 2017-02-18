package com.payments.dao.impl;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

import com.payments.dao.api.BillDAO;
import com.payments.dao.api.CreditCardDAO;
import com.payments.exception.DaoException;
import com.payments.model.CreditCard;
import com.payments.utilites.Constants;
import com.payments.utilites.SqlQueries;
import org.apache.log4j.Logger;

public class CreditCardDAOImpl extends DAOImpl<CreditCard> implements CreditCardDAO {

    private static final Logger LOG = Logger.getLogger(CreditCardDAOImpl.class);
    private static final String PAYMENTS_CREDIT_CARDS = "payments_credit_cards";
    private static final String BILL_ID = "bill_id";
    private static final String ID = "id";
    private static final String USER_ID = "user_id";
    private static final String CARD_NUMBER = "card_number";
    private static final String EXPIRATION_DATE = "expiration_date";

    private static volatile CreditCardDAOImpl instance;

    private static final BillDAO BILL_DAO = BillDAOImpl.getInstance();

    private CreditCardDAOImpl() {
        LOG.debug(String.format(Constants.ENTITY_WAS_CREATED, this.getClass().getSimpleName()));
    }

    /**
     * Method for get instance of {@link CreditCardDAOImpl}
     *
     * @return instance of {@link CreditCardDAOImpl}
     */
    public static CreditCardDAOImpl getInstance() {
        if (instance == null) {
            synchronized (CreditCardDAOImpl.class) {
                if (instance == null) {
                    instance = new CreditCardDAOImpl();
                }
            }
        }
        return instance;
    }

    @Override
    protected List<CreditCard> readObjectFromResultSet(ResultSet resultSet, Connection connection) {
        List<CreditCard> result = new LinkedList<>();
        CreditCard creditCard;
        try {
            while (resultSet.next()) {
                creditCard = new CreditCard();
                creditCard.setId(resultSet.getInt(1));
                creditCard.setExpirationDate(resultSet.getTimestamp(EXPIRATION_DATE));
                creditCard.setBill(BILL_DAO.get(resultSet.getInt(BILL_ID), connection));
                creditCard.setNumberOfCard(resultSet.getString(CARD_NUMBER));
                creditCard.setUserId(resultSet.getInt(USER_ID));
                result.add(creditCard);
            }
        } catch (SQLException e) {
            LOG.error(e.getMessage());
            throw new DaoException(e);
        }
        return result;
    }

    @Override
    protected PreparedStatement createGetStatement(Connection connection, int id) {
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement(String.format(SqlQueries.SELECT_CREDIT_CARD_BY_BILL, BILL_ID, ID));
            preparedStatement.setInt(1, id);
        } catch (SQLException e) {
            LOG.error(e.getMessage());
            throw new DaoException(e);
        }
        return preparedStatement;
    }

    @Override
    protected String createGetAllStatement(String orderBy) {
        return String.format(SqlQueries.SELECT_ALL, PAYMENTS_CREDIT_CARDS, orderBy);
    }

    @Override
    protected PreparedStatement createGetAllByIdStatement(int id, String orderBy, Connection connection) {
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement(String.format(SqlQueries.SELECT_CREDIT_CARD_BY_USER_ID, orderBy));
            preparedStatement.setInt(1, id);
        } catch (SQLException e) {
            LOG.error(e.getMessage());
            throw new DaoException(e);
        }
        return preparedStatement;
    }

    @Override
    protected PreparedStatement createInsertStatement(Connection connection, CreditCard entity) {
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement(SqlQueries.INSERT_CREDIT_CARD, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setTimestamp(1, new Timestamp(System.currentTimeMillis() + Constants.fourYears));
            preparedStatement.setInt(2, entity.getUserId());
            preparedStatement.setInt(3, entity.getBill().getId());
            preparedStatement.setString(4, entity.getBill().getBillNumber());
        } catch (SQLException e) {
            LOG.error(e.getMessage());
            throw new DaoException(e);
        }
        return preparedStatement;
    }

    @Override
    protected PreparedStatement createUpdateStatement(Connection connection, CreditCard entity) {
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement(SqlQueries.UPDATE_CREDIT_CARD);
            preparedStatement.setTimestamp(1, entity.getExpirationDate());
            preparedStatement.setInt(2, entity.getUserId());
            preparedStatement.setInt(3, entity.getBill().getId());
            preparedStatement.setString(4, entity.getBill().getBillNumber());
            preparedStatement.setInt(5, entity.getId());
        } catch (SQLException e) {
            LOG.error(e.getMessage());
            throw new DaoException(e);
        }
        return preparedStatement;
    }

    @Override
    public List<CreditCard> getAllUserCards(int userId, int startPosition, String orderBy, Connection con) {
        List<CreditCard> creditCards;
        PreparedStatement preparedStatement;
        try {
            preparedStatement = con.prepareStatement(String.format(SqlQueries.SELECT_CREDIT_CARD_BY_USER, orderBy));
            preparedStatement.setInt(1, userId);
            ResultSet rs = preparedStatement.executeQuery();
            rs.absolute(startPosition);
            creditCards = readObjectFromResultSet(rs, con);
            releaseResources(rs, LOG, preparedStatement);
        } catch (SQLException e) {
            LOG.error(e.getMessage());
            throw new DaoException(e);
        }

        return creditCards;
    }

    @Override
    public int getCountOfRecords(int userId, Connection connection) {
        int result = 0;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SqlQueries.SELECT_COUNT_OF_CREDIT_CARDS);
            preparedStatement.setInt(1, userId);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                result = rs.getInt(1);
            }
            releaseResources(rs, LOG, preparedStatement);
        } catch (SQLException e) {
            LOG.error(e.getMessage());
            throw new DaoException(e);
        }
        return result;
    }
}
