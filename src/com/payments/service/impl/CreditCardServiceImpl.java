package com.payments.service.impl;

import com.payments.dao.api.CreditCardDAO;
import com.payments.dao.impl.CreditCardDAOImpl;
import com.payments.datasource.DataSource;
import com.payments.exception.DaoException;
import com.payments.model.CreditCard;
import com.payments.service.api.CreditCardService;
import com.payments.utilites.Constants;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.util.List;

/**
 * @author Kostiantyn Dubovik
 */
public class CreditCardServiceImpl implements CreditCardService {

    private static final Logger LOG = Logger.getLogger(CreditCardServiceImpl.class);

    private static final CreditCardDAO CREDIT_CARD_DAO = CreditCardDAOImpl.getInstance();

    private static final DataSource DATA_SOURCE = DataSource.getInstance();

    private static volatile CreditCardServiceImpl instance;

    public static CreditCardServiceImpl getInstance() {
        if (instance == null) {
            synchronized (CreditCardServiceImpl.class) {
                if (instance == null) {
                    instance = new CreditCardServiceImpl();
                }
            }
        }
        return instance;
    }

    private CreditCardServiceImpl() {
        LOG.debug(String.format(Constants.ENTITY_WAS_CREATED, this.getClass().getSimpleName()));
    }

    @Override
    public int getCountOfRecords(int userId) {
        Connection connection = DATA_SOURCE.getConnection();
        int countOfUsers = 0;
        try {
            countOfUsers = CREDIT_CARD_DAO.getCountOfRecords(userId, connection);
        } catch (DaoException ex) {
            rollback(connection, LOG);
        } finally {
            close(connection, LOG);
        }
        return countOfUsers;
    }

    @Override
    public List<CreditCard> getAll(int startPosition, String orderBy) {
        Connection connection = DATA_SOURCE.getConnection();
        List<CreditCard> creditCards = null;
        try {
            creditCards = CREDIT_CARD_DAO.getAllById(startPosition, orderBy, connection);
        } catch (DaoException ex) {
            rollback(connection, LOG);
        } finally {
            close(connection, LOG);
        }
        return creditCards;
    }

    @Override
    public List<CreditCard> getAll(int criteria, int startPosition, String orderBy) {
        Connection connection = DATA_SOURCE.getConnection();
        List<CreditCard> creditCards = null;
        try {
            creditCards = CREDIT_CARD_DAO.getAllById(criteria, startPosition, orderBy, connection);
        } catch (DaoException ex) {
            rollback(connection, LOG);
        } finally {
            close(connection, LOG);
        }
        return creditCards;
    }

    @Override
    public void insert(CreditCard creditCard) {
        LOG.trace("Start inserting: " + creditCard.getNumberOfCard() + " user.");
        Connection connection = DATA_SOURCE.getConnection();
        try {
            CREDIT_CARD_DAO.insert(creditCard, connection);
            commit(connection, LOG);
        } catch (DaoException ex) {
            rollback(connection, LOG);
        } finally {
            close(connection, LOG);
        }
        LOG.trace("Insert of: " + creditCard.getNumberOfCard() + " user success.");
    }

    @Override
    public void update(CreditCard creditCard) {
        Connection connection = DATA_SOURCE.getConnection();
        try {
            CREDIT_CARD_DAO.update(creditCard, connection);
            commit(connection, LOG);
        } catch (DaoException ex) {
            rollback(connection, LOG);
        } finally {
            close(connection, LOG);
        }
    }

    @Override
    public CreditCard getById(int id) {
        Connection connection = DATA_SOURCE.getConnection();
        CreditCard creditCard;
        try {
            creditCard = CREDIT_CARD_DAO.get(id, connection);
        } finally {
            close(connection, LOG);
        }
        return creditCard;
    }

    @Override
    public List<CreditCard> getAllUserCards(int userId, int startPosition, String orderBy) {
        Connection connection = DATA_SOURCE.getConnection();
        List<CreditCard> allUsers = null;
        try {
            allUsers = CREDIT_CARD_DAO.getAllUserCards(userId, startPosition, orderBy, connection);
        } catch (DaoException ex) {
            rollback(connection, LOG);
        } finally {
            close(connection, LOG);
        }
        return allUsers;
    }
}
