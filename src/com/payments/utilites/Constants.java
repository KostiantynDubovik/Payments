package com.payments.utilites;

/**
 * @author Kostiantyn Dubovik
 * @since 1.0
 */
public final class Constants {
    /**
     * Private constructor to limit the creation of objects.
     */
    private Constants(){}

    /**
     * Email settings
     */
    static final String EMAIL_ACCOUNT = "email.account";

    static final String EMAIL_PASSWORD = "email.password";

    /**
     * Database settings
     */
    static final String DRIVER = "driver";
    static final String JDBC_URL = "jdbcUrl";
    static final String LOGIN = "user";
    static final String PASSWORD = "password";
    /**
     * Connection pool settings
     */
    static final String AUTO_COMMIT = "autoCommit";
    static final String MAX_POOL_SIZE = "maxPoolSize";

    /**
     * Captcha setting
     */
    static final String TOTAL_CHARS = "totalChars";
    static final String HEIGHT = "height";
    static final String WIDTH = "width";
    static final String CIRCLE = "circle";
    public static final long fourYears = 126230400000L;


    /**
     * log4j messages
     */
    public static final String CREATING_PREPARED_STATEMENT = "Creating PreparedStatement";
    public static final String PREPARED_STATEMENT_CREATED = "PreparedStatement: [%s] created";
    public static final String PREPARED_STATEMENT_READY = "Prepared statement: [%s] ready";
    public static final String CLOSING_PREPARED_STATEMENT = "Closing PreparedStatement: [%s]";
    public static final String RETURNING_CONNECTION = "Returning connection to pool: [%s]";
    public static final String CLOSING_SUCCESS = "Closing success";
    public static final String CLOSING_RESULT_SET = "Closing ResultSet: [%s]";
    public static final String ENTITY_WAS_INSERTED_TO_DATABASE = "%s [%s] was inserted to database";
    public static final String ENTITY_WAS_UPDATED_IN_DATABASE = "%s [%s] was updated in database";
    public static final String ENTITY_WAS_CREATED = "%s was created";
    public static final String USER = "User";
    public static final String TRY_TO_GET_CONNECTION = "Try to get connection";
    public static final String CONNECTION_RECEIVED = "Connection [%s] received";
    public static final String ORDER_STATUS_CHANGED = "%s: [%s] status changed";
    public static final String SUB_CATEGORY_NAME_CHANGED = "%s with ID: [%s] name changed";


    /**
     * Chars
     */
    public static final String CANDIDATE_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
}
