package com.payments.exception;

import java.sql.SQLException;

/**
 * @author Kostiantyn Dubovik
 */
public class DaoException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 7854393171697762753L;
	private static final String EXCEPTION_MESSAGE = "Can't get connection from Connection Pool";

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Throwable#getMessage()
     */
    public DaoException(final SQLException sqlException) {
        super(sqlException);
    }

    @Override
    public String getMessage() {
        return EXCEPTION_MESSAGE;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Throwable#toString()
     */
    @Override
    public String toString() {
        return EXCEPTION_MESSAGE;
    }

}
