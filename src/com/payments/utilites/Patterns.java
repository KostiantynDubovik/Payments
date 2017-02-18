package com.payments.utilites;

import java.util.regex.Pattern;

class Patterns {

	private Patterns() {

	}
	static final Pattern BILL_NUMBER = Pattern.compile("[0-9]{16}");
	static final Pattern COMMENT_LENGTH = Pattern.compile("[*]{0,80}");
	static final Pattern LOGIN_REGEXP = Pattern.compile("[a-z0-9]{4,10}");
	static final Pattern PASSWORD_REGEXP = Pattern.compile("[a-zA-Z0-9]{4,18}");
	static final Pattern LASTNAME_AND_FIRSTNAME_REGEXP = Pattern.compile("[a-zA-Z]{2,10}");
	static final Pattern EMAIL_REGEXP = Pattern
			.compile("^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

}
