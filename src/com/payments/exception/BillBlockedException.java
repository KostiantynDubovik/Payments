package com.payments.exception;

/**
 * @author Kostiantyn Dubovik
 */
public class BillBlockedException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = -619869579779002561L;
	
	private String exceptionMessage = "Bill %s was blocked";

    public BillBlockedException (String billNumber){
        super(billNumber);
        exceptionMessage = String.format(exceptionMessage, billNumber);
    }


    @Override
    public String getMessage() {
        return exceptionMessage;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Throwable#toString()
     */
    @Override
    public String toString() {
        return exceptionMessage;
    }
}
