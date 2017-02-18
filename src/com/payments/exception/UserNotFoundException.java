package com.payments.exception;

/**
 * @author Kostiantyn Dubovik
 */
class UserNotFoundException extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = -3980315816726215274L;

	public UserNotFoundException (String message){
        super(message);
    }
}
