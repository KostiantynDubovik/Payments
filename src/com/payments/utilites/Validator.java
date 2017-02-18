package com.payments.utilites;

import com.payments.dto.LoginBean;
import com.payments.dto.RegistrationBean;

import java.util.HashMap;
import java.util.Map;


/**
 * @author Kostiantyn Dubovik
 */
public class Validator {

    public static Map<String, String> validateRegistration(RegistrationBean registrationBean) {
        Map<String, String> errorMessages = new HashMap<>();
        checkLogin(registrationBean.getLogin(), errorMessages);
        checkPassword(registrationBean.getPassword(), errorMessages);
        checkPasswordMatch(registrationBean.getPassword(), registrationBean.getPasswordCheck(), errorMessages);
        checkEmail(registrationBean.getEmail(), errorMessages);
        chekFirstname(registrationBean.getFirstName(), errorMessages);
        chekLastname(registrationBean.getLastName(), errorMessages);
        checkCaptcha(registrationBean.getGeneratedCaptcha(), registrationBean.getRetrievedCaptcha(), errorMessages);
        return errorMessages;
    }

    public static Map<String, String> validateLogin(LoginBean loginBean) {
        Map<String, String> errorMessages = new HashMap<>();
        checkLogin(loginBean.getLogin(), errorMessages);
        checkPassword(loginBean.getPassword(), errorMessages);
        checkCaptcha(loginBean.getGeneratedCaptcha(), loginBean.getRetrievedCaptcha(), errorMessages);
        return errorMessages;
    }

    private static void checkLogin(String login, Map<String, String> errorMessages) {
        if (!Patterns.LOGIN_REGEXP.matcher(login).find() || login.isEmpty()) {
            errorMessages.put("invalid_login", "register.massage.invalid_login");
        }
    }

    private static void checkPassword(String password, Map<String, String> errorMessages) {
        if (!Patterns.PASSWORD_REGEXP.matcher(password).find() || password.isEmpty()) {
            errorMessages.put("invalid_password", "register.massage.invalid_password");
        }
    }

    private static void checkPasswordMatch(String password, String passwordCheck, Map<String, String> errorMessages) {
        if (!password.equals(passwordCheck) || passwordCheck.isEmpty()) {
            errorMessages.put("password_not_match", "register.massage.password_not_match");
        }
    }

    private static void checkEmail(String email, Map<String, String> errorMessages) {
        if (!Patterns.EMAIL_REGEXP.matcher(email).find() || email.isEmpty()) {
            errorMessages.put("invalid_email", "register.massage.invalid_mail");
        }
    }

    private static void chekFirstname(String name, Map<String, String> errorMessages) {
        if (!Patterns.LASTNAME_AND_FIRSTNAME_REGEXP.matcher(name).find() || name.isEmpty()) {
            errorMessages.put("invalid_firstname", "register.massage.invalid_firstname");
        }
    }

    private static void chekLastname(String name, Map<String, String> errorMessages) {
        if (!Patterns.LASTNAME_AND_FIRSTNAME_REGEXP.matcher(name).find() || name.isEmpty()) {
            errorMessages.put("invalid_lastname", "register.massage.invalid_lastname");
        }
    }

    private static void checkCaptcha(String generatedCaptcha, String retrievedCaptcha, Map<String, String> errorMessages) {
        System.err.println("generated captcha" + generatedCaptcha);
        System.err.println("retrieved captcha" + retrievedCaptcha);
        if (!generatedCaptcha.equalsIgnoreCase(retrievedCaptcha) || retrievedCaptcha == null) {
            errorMessages.put("invalid_captcha", "massage.wrong_captcha");
        }
    }
}
