package com.payments.dao.impl;

import com.payments.dao.api.BillDAO;
import com.payments.dao.api.CreditCardDAO;
import com.payments.dao.api.UserDAO;
import com.payments.exception.DaoException;
import com.payments.model.User;
import com.payments.utilites.Constants;
import com.payments.utilites.SqlQueries;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.sql.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.payments.model.enums.UserRole.chooseRole;

/**
 * @author Kostiantyn Dubovik
 */
public final class UserDAOImpl extends DAOImpl<User> implements UserDAO {

    private static final Logger LOG = Logger.getLogger(UserDAOImpl.class);
    public static final int START_POSITION = 0;

    private static volatile UserDAOImpl instance;

    private static final CreditCardDAO CREDIT_CARD_DAO = CreditCardDAOImpl.getInstance();

    private static final BillDAO BILL_DAO = BillDAOImpl.getInstance();

    private static final String LOGIN ="login";
    private static final String EMAIL ="email";
    private static final String LANGUAGE ="language";
    private static final String BLOCKED ="blocked";
    private static final String PASSWORD ="password";
    private static final String ID ="id";
    private static final String FIRSTNAME ="firstname";
    private static final String LASTNAME ="lastname";
    private static final String NUMBER ="number";
    private static final String ROLE ="role";


    private UserDAOImpl() {
        LOG.debug(String.format(Constants.ENTITY_WAS_CREATED, this.getClass().getSimpleName()));
    }

    /**
     * Method for get instance of {@link UserDAOImpl}
     *
     * @return instance of {@link UserDAOImpl}
     */
    public static UserDAOImpl getInstance() {
        if (instance == null) {
            synchronized (UserDAOImpl.class) {
                if (instance == null) {
                    instance = new UserDAOImpl();
                }
            }
        }
        return instance;
    }


    /**
     * @see DAOImpl#readObjectFromResultSet(ResultSet, Connection)
     */
    @Override
    protected List<User> readObjectFromResultSet(ResultSet resultSet, Connection connection) {

        User user;
        List<User> users = new LinkedList<>();

        try {
            if (resultSet != null) {
                while (resultSet.next()) {
                    user = new User();
                    user.setId(resultSet.getInt(ID));
                    user.setLogin(resultSet.getString(LOGIN));
                    user.setPassword(resultSet.getString(PASSWORD));
                    user.setFirstName(resultSet.getString(FIRSTNAME));
                    user.setLastName(resultSet.getString(LASTNAME));
                    user.setRole(chooseRole(resultSet.getInt(ROLE)));
                    user.setBlocked(resultSet.getBoolean(BLOCKED));
                    user.setLanguage(resultSet.getString(LANGUAGE));
                    user.setEmail(resultSet.getString(EMAIL));
                    user.setCreditCards(CREDIT_CARD_DAO.getAllUserCards(user.getId(), START_POSITION, ID, connection));
                    user.setBills(BILL_DAO.getAllById(user.getId(), START_POSITION, NUMBER, connection));
                    user.setUserBalance(getUserBalance(user.getId(), connection));
                    users.add(user);
                }
            }
        } catch (SQLException e) {
            LOG.error(e.getMessage());
            throw new DaoException(e);
        }
        return users;
    }

    @Override
    protected PreparedStatement createGetStatement(Connection connection, int userId) {
        return getUsers(userId, ID, ID, connection);
    }

    @Override
    protected PreparedStatement createGetAllByIdStatement(int roleId, String orderBy, Connection connection) {
        return getUsers(roleId, orderBy, ROLE, connection);
    }

    /**
     *
     * @param id of user or role
     * @param orderBy what order use for select
     * @param criteria by what choose user
     * @param connection from database
     * @return preparedStatement
     */
    private PreparedStatement getUsers(int id, String orderBy, String criteria, Connection connection){
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement(String.format(SqlQueries.SELECT_USER, criteria, orderBy));
            preparedStatement.setInt(1, id);
        } catch (SQLException e) {
            LOG.error(e.getMessage());
            throw new DaoException(e);
        }
        return preparedStatement;
    }

    /**
     * @see DAOImpl#createGetAllStatement(String)
     */
    @Override
    protected String createGetAllStatement(String orderBy) {
        return String.format(SqlQueries.SELECT_ALL_USERS, orderBy);
    }

    @Override
    protected PreparedStatement createInsertStatement(Connection connection, User entity) {
        PreparedStatement preparedStatement;
        try {
            LOG.debug(Constants.CREATING_PREPARED_STATEMENT);
            preparedStatement = connection.prepareStatement(SqlQueries.INSERT_USER, PreparedStatement.RETURN_GENERATED_KEYS);
            LOG.debug(String.format(Constants.PREPARED_STATEMENT_CREATED, preparedStatement));
            preparedStatement.setString(1, entity.getLogin());
            preparedStatement.setString(2, entity.getPassword());
            preparedStatement.setString(3, entity.getFirstName());
            preparedStatement.setString(4, entity.getLastName());
            preparedStatement.setString(5, entity.getLanguage());
            preparedStatement.setString(6, entity.getEmail());
            LOG.trace(String.format(Constants.PREPARED_STATEMENT_READY, preparedStatement));
        } catch (SQLException e) {
            LOG.error(e.getMessage());
            throw new DaoException(e);
        }
        return preparedStatement;
    }

    @Override
    public String[] getLanguageAndEmail(int id, Connection connection) {
        PreparedStatement preparedStatement;
        String[] languageAndEmail = new String[2];
        try {
            preparedStatement = connection.prepareStatement(SqlQueries.SELECT_LANGUAGE_AND_EMAIL);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                languageAndEmail[0] = resultSet.getString(LANGUAGE);
                languageAndEmail[1] = resultSet.getString(EMAIL);
            }
        } catch (SQLException e) {
            LOG.error(e.getMessage());
            throw new DaoException(e);
        }
        return languageAndEmail;
    }

    @Override
    public BigDecimal getUserBalance(int userId, Connection connection) {
        PreparedStatement preparedStatement;
        BigDecimal userBalance = null;
        try {
            preparedStatement = connection.prepareStatement(SqlQueries.SELECT_USER_BALANCE);
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                userBalance = resultSet.getBigDecimal(1);
            }
        } catch (SQLException e) {
            LOG.error(e.getMessage());
            throw new DaoException(e);
        }
        return userBalance;
    }

    @Override
    protected PreparedStatement createUpdateStatement(Connection connection, User entity) {
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement(SqlQueries.UPDATE_USER);
            preparedStatement.setString(1, entity.getFirstName());
            preparedStatement.setString(2, entity.getLastName());
            preparedStatement.setString(3, entity.getPassword());
            preparedStatement.setString(4, entity.getLanguage());
        } catch (SQLException e) {
            LOG.error(e.getMessage());
            throw new DaoException(e);
        }
        return preparedStatement;
    }

    /**
     * @see UserDAO#getUser(String, Connection)
     */
    @Override
    public User getUser(String login, Connection con) {
        User user;
        try {
            PreparedStatement ps = con.prepareStatement(String.format(SqlQueries.SELECT_USER, LOGIN, ID));
            ps.setString(1, login);
            ResultSet rs = ps.executeQuery();
            user = readObjectFromResultSet(rs, con).get(0);
            releaseResources(rs, LOG, ps);
        } catch (SQLException e) {
            LOG.error(e.getMessage());
            throw new DaoException(e);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
        return user;
    }

    /**
     * @see UserDAO#setBlocked(boolean, String, Connection)
     */
    @Override
    public void setBlocked(boolean blocked, String login, Connection con) {
        try {
            PreparedStatement ps = con.prepareStatement(SqlQueries.SET_USER_BLOCKED);
            ps.setBoolean(1, blocked);
            ps.setString(2, login);
            ps.executeUpdate();
            releaseResources(null, LOG, ps);
        } catch (SQLException e) {
            LOG.error(e.getMessage());
            throw new DaoException(e);
        }
    }



    @Override
    public Map<String, Boolean> existsLoginAndMail(String login, String email, Connection connection) {
        Map<String, Boolean> result = new HashMap<>();
        try {
            PreparedStatement ps = connection.prepareStatement(SqlQueries.EXISTS_LOGIN);
            ps.setString(1, login);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                result.put(LOGIN, true);
            } else {
                result.put(LOGIN, false);
            }
            releaseResources(rs, LOG, ps);

            ps = connection.prepareStatement(SqlQueries.EXISTS_EMAIL);
            ps.setString(1, email);
            rs = ps.executeQuery();
            if (rs.next()) {
                result.put(EMAIL, true);
            } else {
                result.put(EMAIL, false);
            }
            releaseResources(rs, LOG, ps);
        } catch (SQLException e) {
            LOG.error(e.getMessage());
            throw new DaoException(e);
        }
        return result;
    }

    /**
     * @param userRoleId of needed user role
     * @return count of needed
     */
    @Override
    public synchronized int getCountOfRecords(int userRoleId, Connection con) {
        int result = START_POSITION;
        try {
            PreparedStatement ps = con.prepareStatement(SqlQueries.SELECT_COUNT);
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
}
