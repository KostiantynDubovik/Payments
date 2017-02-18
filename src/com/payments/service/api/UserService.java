package com.payments.service.api;

import com.payments.model.User;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author Kostiantyn Dubovik
 */
public interface UserService extends Service<User>{

    User getUser(String login);

    void setBlocked(String login);

    void setUnblocked(String login);

    Map<String, Boolean> existsLoginAndMail(String login, String email);

}
