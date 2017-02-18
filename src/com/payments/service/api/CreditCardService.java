package com.payments.service.api;

import com.payments.model.CreditCard;

import java.util.List;

/**
 * @author Kostiantyn Dubovik
 */
public interface CreditCardService extends Service<CreditCard>{
    List<CreditCard> getAllUserCards(int userId, int startPosition, String orderBy);
}
