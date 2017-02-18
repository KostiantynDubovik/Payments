package com.payments.web.servlets;

import com.payments.dto.RegistrationBean;
import com.payments.model.User;
import com.payments.service.impl.UserServiceImpl;
import com.payments.utilites.Validator;

import java.io.IOException;

import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class RegistrationServlet
 */
public class RegistrationServlet extends HttpServlet {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    private static final UserServiceImpl USER_SERVICE = UserServiceImpl.getInstance();
    private static final String LOGIN = "login";
    private static final String ERROR_MESSAGES = "error_messages";
    private static final String EXIST_LOGIN = "exist_login";
    private static final String EXIST_MAIL = "exist_mail";
    private static final String EMAIL = "email";
    private static final String REGISTER_MASSAGE_EXIST_MAIL = "register.massage.exist_mail";
    private static final String REGISTER_MASSAGE_EXIST_LOGIN = "register.massage.exist_login";
    private static final String PASSWORD = "password";
    private static final String PASSWORD_CHECK = "password_check";
    private static final String FIRSTNAME = "firstname";
    private static final String LASTNAME = "lastname";
    private static final String JCAPTCHA = "jcaptcha";
    private static final String LANGUAGE = "language";
    private static final String DNS_SECURITY_CODE = "dns_security_code";
    private static final String JSP_REFERENCE = "WEB-INF/register.jsp";

    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegistrationServlet() {

    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest, HttpServletResponse)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher(JSP_REFERENCE).forward(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest, HttpServletResponse)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        session.removeAttribute(ERROR_MESSAGES);
        RegistrationBean registrationBean = extractRegistrationBeanFromRequest(request);

        Map<String, Boolean> existsLoginAndMails = USER_SERVICE.existsLoginAndMail(registrationBean.getLogin(), registrationBean.getEmail());

        Map<String, String> errorMessages = Validator.validateRegistration(registrationBean);
        if (existsLoginAndMails.get(LOGIN)) {
            errorMessages.put(EXIST_LOGIN, REGISTER_MASSAGE_EXIST_LOGIN);
        }
        if (existsLoginAndMails.get(EMAIL)) {
            errorMessages.put(EXIST_MAIL, REGISTER_MASSAGE_EXIST_MAIL);
        }

        if (errorMessages.isEmpty()) {
            User user = extractUserFromRegistrationBean(registrationBean);
            USER_SERVICE.insert(user);
            response.sendRedirect("/user");
        } else {
            session.setAttribute(ERROR_MESSAGES, errorMessages);
            response.sendRedirect("/register");
        }
    }

    private RegistrationBean extractRegistrationBeanFromRequest(HttpServletRequest request) {
        RegistrationBean registrationBean = new RegistrationBean();
        registrationBean.setLogin(request.getParameter(LOGIN));
        registrationBean.setPassword(request.getParameter(PASSWORD));
        registrationBean.setPasswordCheck(request.getParameter(PASSWORD_CHECK));
        registrationBean.setFirstName(request.getParameter(FIRSTNAME));
        registrationBean.setLastName(request.getParameter(LASTNAME));
        registrationBean.setEmail(request.getParameter(EMAIL));
        registrationBean.setRetrievedCaptcha(request.getParameter(JCAPTCHA));
        registrationBean.setGeneratedCaptcha((String) request.getSession(false).getAttribute(DNS_SECURITY_CODE));
        registrationBean.setLanguage(request.getSession(false).getAttribute(LANGUAGE).toString());
        return registrationBean;
    }

    private User extractUserFromRegistrationBean(RegistrationBean registrationBean) {
        User user = new User();
        user.setLogin(registrationBean.getLogin());
        user.setPassword(registrationBean.getPassword());
        user.setFirstName(registrationBean.getFirstName());
        user.setLastName(registrationBean.getLastName());
        user.setLanguage(registrationBean.getLanguage());
        user.setEmail(registrationBean.getEmail());
        return user;
    }
}
