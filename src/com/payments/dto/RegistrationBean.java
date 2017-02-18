package com.payments.dto;

/**
 * @author Kostiantyn Dubovik
 */
public class RegistrationBean {

    private String login;
    private String password;
    private String passwordCheck;
    private String firstName;
    private String lastName;
    private String email;
    private String retrievedCaptcha;
    private String generatedCaptcha;
    private String language;

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

    public String getPasswordCheck() {
        return passwordCheck;
    }

    public void setPasswordCheck(String passwordCheck) {
        this.passwordCheck = passwordCheck;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
