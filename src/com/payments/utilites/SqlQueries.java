package com.payments.utilites;

/**
 * @author Kostiantyn Dubovik
 */
public class SqlQueries {

    private SqlQueries(){

    }

    /**
     * Database scripts
     */
    public static final String SELECT_ALL = "SELECT * FROM `payments`.`%s` ORDER BY %s;";
    public static final String SELECT_COUNT_OF_INCOMING_PAYMENTS = "SELECT count(*) FROM `payments`.`payments_incoming_payments` `ip` JOIN `payments`.`payments_bills` `pb` WHERE `ip`.`to_bill_id`=`pb`.`id` AND `pb`.`user_id`=?;";
    public static final String SELECT_COUNT_OF_OUTGOING_PAYMENTS = "SELECT count(*) FROM `payments`.`payments_outgoing_payments` `op` JOIN `payments`.`payments_bills` `pb` WHERE `op`.`from_bill_id`=`pb`.`id` AND `pb`.`user_id`=?;";
    public static final String SELECT_INCOMING_PAYMENT_BY_BILL_NUMBERS =  "SELECT * FROM `payments`.`payments_incoming_payments` `ip` JOIN `payments`.`payments_bills` `pb` WHERE `ip`.`to_bill_id`=`pb`.`id` AND `ip`.`from_bill`=? AND `pb`.`number`=?;";

    public static final String SELECT_OUTGOING_PAYMENT_BY_BILL_NUMBERS =  "SELECT * FROM `payments`.`payments_outgoing_payments` `op` JOIN `payments`.`payments_bills` `pb` WHERE `op`.`from_bill_id`=`pb`.`id` AND `op`.`to_bill`=? AND `pb`.`number`=?;";
    public static final String SELECT_COUNT_OF_CREDIT_CARDS = "SELECT count(*) FROM `payments`.`payments_credit_cards` WHERE `user_id`=?;";
    public static final String UPDATE_CREDIT_CARD = "UPDATE `payments`.`payments_credit_cards` SET expiration_date=?, user_id=?, bill_id=?, card_number=? WHERE `id`=?;";
    public static final String SELECT_CREDIT_CARD_BY_USER = "SELECT * FROM `payments`.`payments_credit_cards` WHERE `user_id`=? ORDER BY %s;";
    public static final String SELECT_CREDIT_CARD_BY_BILL = "SELECT * FROM `payments`.`payments_credit_cards` WHERE `%s`=? ORDER BY `%s`;";
    public static final String SELECT_CREDIT_CARD_BY_USER_ID = "SELECT * FROM `payments`.`payments_credit_cards` WHERE `user_id`=? ORDER BY %s;";
    public static final String INSERT_CREDIT_CARD = "INSERT INTO `payments`.`payments_credit_cards` (expiration_date, user_id, bill_id, card_number) VALUES (?,?,?,?);";
    public static final String SELECT_OUTGOING_PAYMENT_BY_ID = "SELECT * FROM `payments`.`payments_outgoing_payments` `p` JOIN `payments`.`payments_bills` `b` WHERE b.id=p.from_bill_id AND p.id=?;";
    public static final String SELECT_INCOMING_PAYMENT_BY_ID = "SELECT * FROM `payments`.`payments_incoming_payments` `p` JOIN `payments`.`payments_bills` `b` WHERE b.id=p.to_bill_id AND p.id=?;";
    public static final String SELECT_OUTGOING_PAYMENT_BY_USER_ID = "SELECT * FROM `payments`.`payments_outgoing_payments` `p` JOIN `payments`.`payments_bills` `b` WHERE `p`.`from_bill_id`=`b`.`id` AND `b`.`user_id`=? ORDER BY %s;";
    public static final String SELECT_INCOMING_PAYMENT_BY_BILL_ID = "SELECT * FROM `payments`.`payments_incoming_payments` `p` JOIN `payments`.`payments_bills` `b` WHERE `p`.`to_bill_id`=`b`.`id` AND `b`.`user_id`=? ORDER BY %s;";
    public static final String INSERT_INCOMING_PAYMENT = "INSERT INTO `payments`.`payments_incoming_payments` (from_bill, to_bill_id, comment, date, sum, commission) VALUES (?,?,?,?,?,?);";
    public static final String INSERT_OUTGOING_PAYMENT = "INSERT INTO `payments`.`payments_outgoing_payments` (from_bill_id, to_bill, comment, date, sum, commission) VALUES (?,?,?,?,?,?);";
    public static final String UPDATE_BILL = "UPDATE `payments`.`payments_bills` SET balance=?, blocked=?, need_to_unblock=? WHERE id=?;";
    public static final String INSERT_BILL = "INSERT INTO `payments`.`payments_bills` (user_id, number, name, balance) VALUES (?,?,?,?);";
    public static final String SELECT_BILLS_BY_USER_ID = "SELECT * FROM `payments`.`payments_bills` WHERE %s ORDER BY %s;";
    public static final String SELECT_BILL = "SELECT * FROM `payments`.`payments_bills` WHERE id=?;";
    public static final String SELECT_LANGUAGE_AND_EMAIL = "SELECT `language`,`email` FROM `payments`.`payments_users` WHERE `id`=?;";
    public static final String SELECT_USER_BALANCE = "select sum(balance) from payments_bills where user_id=?;";
    public static final String SELECT_COUNT_OF_BILLS = "SELECT count(*) FROM `payments`.`payments_bills` WHERE `user_id`=?;";
    public static final String GET_BILL_BY_NUMBER = "SELECT * FROM `payments`.`payments_bills` WHERE number=?;";
    public static final String UPDATE = "UPDATE `payments`.`%s` SET %s WHERE `id` =?;";
    public static final String DELETE = "DELETE FROM `payments`.`%s` WHERE `id` =?;";
    public static final String SET_USER_BLOCKED = "UPDATE payments_users SET blocked=? WHERE login=?;";
    public static final String SELECT_COUNT = "SELECT count(*) FROM payments_users WHERE role=2;";
    public static final String EXISTS_LOGIN = "SELECT `login` FROM `payments`.`payments_users` WHERE `login`=?;";
    public static final String EXISTS_EMAIL = "SELECT `email` FROM `payments`.`payments_users` WHERE `email`=?;";
    public static final String SELECT_USER = "SELECT * FROM `payments`.`payments_users` `u` JOIN `payments`.`payments_roles` `r` WHERE `r`.`role_id` = `u`.`role`  AND  `u`.`%s` = ? ORDER BY `%s`;";
    public static final String SELECT_ALL_USERS = "SELECT * FROM `payments`.`payments_users` `u` JOIN `payments`.`payments_roles` `r` WHERE `r`.`role_id` = `u`.`role` ORDER BY `%s`;";
    public static final String INSERT_USER = "INSERT INTO `payments`.`payments_users` (`login`, `password`, `role`, `firstname`, `lastname`, `blocked`, `language`,`email`) VALUES (?, ?, '2', ?, ?, '0', ?, ?);";
    public static final String UPDATE_USER = "UPDATE `payments`.`payments_users` SET `firstname`=?, `lastname`=?, `password`=?, `language`=? WHERE `login`=?;";
}
