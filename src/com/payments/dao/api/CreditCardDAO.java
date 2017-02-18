package com.payments.dao.api;

import com.payments.model.CreditCard;

import java.sql.Connection;
import java.util.List;

/**
 * @author Kostiantyn Dubovik
 */
public interface CreditCardDAO extends DAO<CreditCard> {

    List<CreditCard> getAllUserCards(int userId, int startPosotion, String orderBy, Connection con);
}
