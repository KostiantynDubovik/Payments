package com.payments.utilites;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Kostiantyn Dubovik
 */
final class CaptchaCheck {

    public static final String CAPTCHA = "";
    public static final String DNS_SECURITY_CODE = "dns_security_code";
    public static final String JCAPTCHA = "jcaptcha";

    /**
     * Private constructor for do not possible create an object of this class
     */
    private CaptchaCheck() {

    }

    private final static Logger LOG = Logger.getLogger(CaptchaCheck.class);

    /**
     * Checking inputed captcha with generated
     *
     * @param request where from need pick captcha
     * @return complete check or not
     */
    public static boolean checkCaptcha(HttpServletRequest request) {
        Object o = request.getSession().getAttribute(DNS_SECURITY_CODE);
        String captcha = CAPTCHA;
        if (o != null) {
            captcha = o.toString();
            System.err.println(captcha);
        }
        String retrievedCaptcha = request.getParameter(JCAPTCHA);
        return captcha.equalsIgnoreCase(retrievedCaptcha);
    }
}
