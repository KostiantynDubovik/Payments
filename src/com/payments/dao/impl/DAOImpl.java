package com.payments.dao.impl;

import com.payments.dao.api.DAO;
import com.payments.exception.DaoException;
import com.payments.model.*;
import com.payments.utilites.SqlQueries;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @param <E>
 * @author Kostiantyn Dubovik
 */
abstract class DAOImpl<E extends Entity> implements DAO<E> {

    /**
     *
     */
    private final static Logger LOG = Logger.getLogger(DAOImpl.class);
    private static final String PAYMENTS_INCOMING_PAYMENTS = "payments_incoming_payments";
    private static final String PAYMENTS_OUTGOING_PAYMENTS = "payments_outgoing_payments";
    private static final String PAYMENTS_BILLS = "payments_bills";
    private static final String PAYMENTS_USERS = "payments_users";
    private static final String PAYMENTS_CREDIT_CARDS = "payments_credit_cards";


    /**
     * @see DAO#insert(Entity, Connection)
     */
    @Override
    public synchronized void insert(E entity, Connection con) {
        try {
            PreparedStatement insertStatement = createInsertStatement(con, entity);
            insertStatement.executeUpdate();
            ResultSet rs = insertStatement.getGeneratedKeys();
            rs.beforeFirst();
            if (rs.next()) {
                entity.setId(rs.getInt(1));
            }
            releaseResources(rs, LOG, insertStatement);
        } catch (SQLException e) {
            LOG.error(e.getMessage());
            throw new DaoException(e);
        }
    }

    /**
     * @see DAO#update(Entity, Connection)
     */
    @Override
    public synchronized void update(E entity, Connection connection) {
        try {
            PreparedStatement updateStatement = createUpdateStatement(connection, entity);
            updateStatement.executeUpdate();
            releaseResources(null, LOG, updateStatement);
        } catch (SQLException e) {
            LOG.error(e.getMessage());
            throw new DaoException(e);
        }
    }

    /**
     * @see DAO#delete(int, Class, Connection)
     */
    @Override
    public synchronized void delete(int id, Class<E> aClass, Connection connection) {
        try {
            PreparedStatement deleteStatement = connection.prepareStatement(String.format(SqlQueries.DELETE, chooseTable(aClass)));
            deleteStatement.executeUpdate();
            releaseResources(null, LOG, deleteStatement);
        } catch (SQLException e) {
            LOG.error(e.getMessage());
            throw new DaoException(e);
        }
    }

    @Override
    public synchronized E get(int id, Connection connection) {
        E entity;
        try {
            PreparedStatement getStatement = createGetStatement(connection, id);
            ResultSet resultSet = getStatement.executeQuery();
            entity = readObjectFromResultSet(resultSet, connection).get(0);
            releaseResources(resultSet, LOG, getStatement);
        } catch (SQLException e) {
            LOG.error(e.getMessage());
            throw new DaoException(e);
        } catch (IndexOutOfBoundsException e) {
            LOG.error(e.getMessage());
            entity = null;
        }
        return entity;
    }

    /**
     * @see DAO#getAllById(int, String, Connection con)
     */
    @Override
    public synchronized List<E> getAllById(int startPosition, String orderBy, Connection con) {
        List<E> entities;
        try {
            PreparedStatement getAllStatement = con.prepareStatement(createGetAllStatement(orderBy));
            ResultSet resultSet = getAllStatement.executeQuery();
            resultSet.absolute(startPosition);
            entities = readObjectFromResultSet(resultSet, con);
            releaseResources(resultSet, LOG, getAllStatement);
        } catch (SQLException e) {
            LOG.error(e.getMessage());
            throw new DaoException(e);        }
        return entities;
    }

    @Override
    public synchronized List<E> getAllById(int id, int startPosition, String orderBy, Connection con) {
        List<E> result;
        try {
            PreparedStatement getAllByIdStatement = createGetAllByIdStatement(id, orderBy, con);
            ResultSet resultSet = getAllByIdStatement.executeQuery();
            resultSet.absolute(startPosition);
            result = readObjectFromResultSet(resultSet, con);
            releaseResources(resultSet, LOG, getAllByIdStatement);
        } catch (SQLException e) {
            LOG.error(e.getMessage());
            throw new DaoException(e);
        }
        return result;
    }

    /**
     * Reads entity from {@link ResultSet}
     *
     * @param resultSet from whence entities will be read
     * @return read entities
     */
    protected abstract List<E> readObjectFromResultSet(ResultSet resultSet, Connection connection);

    /**
     * Method chooseStatus table where from being read entities
     *
     * @param aClass using for chooseStatus needed table
     * @return name of table where is needed entities
     */
    private String chooseTable(Class<?> aClass) {
        String result = null;
        if (CreditCard.class.equals(aClass)) {
            result = PAYMENTS_CREDIT_CARDS;
        } else if (User.class.equals(aClass)) {
            result = PAYMENTS_USERS;
        } else if (Bill.class.equals(aClass)) {
            result = PAYMENTS_BILLS;
        } else if (OutgoingPayment.class.equals(aClass)) {
            result = PAYMENTS_OUTGOING_PAYMENTS;
        } else if (IncomingPayment.class.equals(aClass)) {
            result = PAYMENTS_INCOMING_PAYMENTS;
        }
        return result;
    }


    /**
     * @param connection     s
     * @param id      s
     * @return s
     */
    protected abstract PreparedStatement createGetStatement(Connection connection, int id);

    /**
     * @param orderBy to set order of results
     * @return s
     */
    protected abstract String createGetAllStatement(String orderBy);

    /**
     * @param id      of parent entity
     * @param orderBy to set order of results
     * @param connection     connection to database
     * @return s
     */
    protected abstract PreparedStatement createGetAllByIdStatement(int id, String orderBy, Connection connection);

    /**
     * @param connection    connection to database
     * @param entity which need to insert to database
     * @return ready prepared statement
     */
    protected abstract PreparedStatement createInsertStatement(Connection connection, E entity);

    /**
     * @param connection    connection to database
     * @param entity with updates
     * @return ready prepared statement
     */
    protected abstract PreparedStatement createUpdateStatement(Connection connection, E entity);

    /**
     * Method for closing {@link ResultSet} and {@link PreparedStatement} from methods using it.
     *
     * @param resultSet     {@link ResultSet}
     * @param preparedStatements     {@link PreparedStatement}
     * @param logger {@link Logger} of current class
     */
    void releaseResources(ResultSet resultSet, Logger logger, PreparedStatement... preparedStatements) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatements != null) {
                for (PreparedStatement preparedStatement : preparedStatements) {
                    preparedStatement.close();
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DaoException(e);
        }
    }

}