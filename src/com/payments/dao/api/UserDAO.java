package com.payments.dao.api;

import com.payments.model.User;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Map;

/**
 * @author Kostiantyn Dubovik
 */
public interface UserDAO extends DAO<User> {
    /**
     * Using for user authentication
     *
     * @param login    of user
     * @return - needed User
     */
    User getUser(String login, Connection con);

    /**
     * Set user blocked or unblocked
     *
     * @param blocked sets blocking status of user
     */
    void setBlocked(boolean blocked, String userLogin, Connection con);

    Map<String, Boolean> existsLoginAndMail(String login, String email, Connection connection);

    /**
     *
     * @param id of needed order role
     * @return count of needed
     */
    int getCountOfRecords(int id, Connection con);

    String[] getLanguageAndEmail(int id, Connection connection);

    BigDecimal getUserBalance(int userId, Connection connection);
}
