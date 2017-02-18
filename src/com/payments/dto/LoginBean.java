package com.payments.dto;

/**
 * @author Kostiantyn Dubovik
 */
public class LoginBean {
    private String login;
    private String password;
    private String retrievedCaptcha;
    private String generatedCaptcha;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRetrievedCaptcha() {
        return retrievedCaptcha;
    }

    public void setRetrievedCaptcha(String retrievedCaptcha) {
        this.retrievedCaptcha = retrievedCaptcha;
    }

    public String getGeneratedCaptcha() {
        return generatedCaptcha;
    }

    public void setGeneratedCaptcha(String generatedCaptcha) {
        this.generatedCaptcha = generatedCaptcha;
    }
}
