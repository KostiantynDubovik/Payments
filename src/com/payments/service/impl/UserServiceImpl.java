package com.payments.service.impl;

import com.payments.dao.api.UserDAO;
import com.payments.dao.impl.UserDAOImpl;
import com.payments.datasource.DataSource;
import com.payments.exception.DaoException;
import com.payments.utilites.MailHelper;
import com.payments.model.User;
import com.payments.service.api.UserService;
import com.payments.utilites.Constants;
import com.payments.utilites.PasswordAuthentication;
import org.apache.log4j.Logger;

import javax.mail.MessagingException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.*;

/**
 * @author Kostiantyn Dubovik
 */
public final class UserServiceImpl implements UserService {

    /**
     *
     */
    private static volatile UserServiceImpl instance;


    private final static DataSource DATA_SOURCE = DataSource.getInstance();

    /**
     *
     */
    private static final UserDAO USER_DAO = UserDAOImpl.getInstance();

    /**
     *
     */
    private static final Logger LOG = Logger.getLogger(UserServiceImpl.class);

    /**
     *
     */
    private static final int ACTIVATION_CODE_LENGTH = 17;

    /**
     *
     */
    private UserServiceImpl() {
        LOG.debug(String.format(Constants.ENTITY_WAS_CREATED, this.getClass().getSimpleName()));
    }

    /**
     * @return instance of UserService
     */
    public static UserServiceImpl getInstance() {
        if (instance == null) {
            synchronized (UserServiceImpl.class) {
                if (instance == null) {
                    instance = new UserServiceImpl();
                }
            }
        }
        return instance;
    }

    /**
     * @param user for insert to database
     */
    @Override
    public void insert(User user) {
        Connection connection = DATA_SOURCE.getConnection();
        user.setPassword(PasswordAuthentication.generateStrongPasswordHash(user.getPassword()));
        try {
            USER_DAO.insert(user, connection);
            commit(connection,LOG);
            MailHelper.sendMailOfRegistrationGreeting(user);
        } catch (MessagingException e) {
            LOG.error(e.getMessage());
        } catch (DaoException ex) {
            rollback(connection, LOG);
        } finally {
            close(connection, LOG);
        }
    }

    @Override
    public void update(User user) {
        Connection connection = DATA_SOURCE.getConnection();
        try {
            USER_DAO.update(user, connection);
            MailHelper.sendMailOfRegistrationGreeting(user);
            commit(connection,LOG);
        } catch (MessagingException e) {
            LOG.error(e.getMessage());
        } catch (DaoException ex) {
            rollback(connection, LOG);
        } finally {
            close(connection, LOG);
        }
    }

    @Override
    public User getById(int id) {
        Connection connection = DATA_SOURCE.getConnection();
        User user;
        try {
            user = USER_DAO.get(id, connection);
        } finally {
            close(connection, LOG);
        }
        return user;
    }

    @Override
    public List<User> getAll(int startPosition, String orderBy) {
        Connection connection = DATA_SOURCE.getConnection();
        List<User> allUsers = null;
        try {
            allUsers = USER_DAO.getAllById(startPosition, orderBy, connection);
        } catch (DaoException ex) {
            rollback(connection, LOG);
        } finally {
            close(connection, LOG);
        }
        return allUsers;
    }

    @Override
    public List<User> getAll(int roleId, int startPosition, String orderBy) {
        Connection connection = DATA_SOURCE.getConnection();
        List<User> allUsers = null;
        try {
            allUsers = USER_DAO.getAllById(roleId,startPosition, orderBy, connection);
        } catch (DaoException ex) {
            rollback(connection, LOG);
        } finally {
            close(connection, LOG);
        }
        return allUsers;
    }

    @Override
    public int getCountOfRecords(int userRoleId) {
        Connection connection = DATA_SOURCE.getConnection();
        int countOfUsers = 0;
        try {
            countOfUsers = USER_DAO.getCountOfRecords(userRoleId, connection);
        } catch (DaoException ex) {
            rollback(connection, LOG);
        } finally {
            close(connection, LOG);
        }
        return countOfUsers;
    }

    /**
     * @param login for find user
     * @return user with login
     */
    @Override
    public User getUser(String login) {
        Connection connection = DATA_SOURCE.getConnection();
        User user = null;
        try {
            user = USER_DAO.getUser(login, connection);
        } catch (DaoException ex) {
            rollback(connection, LOG);
        } finally {
            close(connection, LOG);
        }
        return user;
    }

    @Override
    public void setBlocked(String login) {
        Connection connection = DATA_SOURCE.getConnection();
        try {
            USER_DAO.setBlocked(true, login, connection);
            commit(connection,LOG);
        } catch (DaoException ex) {
            rollback(connection, LOG);
        } finally {
            close(connection, LOG);
        }
    }

    @Override
    public void setUnblocked(String login) {
        Connection connection = DATA_SOURCE.getConnection();
        try {
            USER_DAO.setBlocked(false, login, connection);
            commit(connection,LOG);
        } catch (DaoException ex) {
            rollback(connection, LOG);
        } finally {
            close(connection, LOG);
        }
    }

    @Override
    public Map<String, Boolean> existsLoginAndMail(String login, String email) {
        Map<String, Boolean> result = null;
        Connection connection = DATA_SOURCE.getConnection();
        try {
            result = USER_DAO.existsLoginAndMail(login, email, connection);
        } catch (DaoException ex) {
            rollback(connection, LOG);
        } finally {
            close(connection, LOG);
        }
        return result;
    }

}
