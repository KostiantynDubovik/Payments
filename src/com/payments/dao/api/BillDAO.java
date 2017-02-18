package com.payments.dao.api;

import com.payments.model.Bill;

import java.sql.Connection;

/**
 * @author Kostiantyn Dubovik
 */
public interface BillDAO extends DAO <Bill> {
    Bill getBillByNumber(String billNumber, Connection con);
}
