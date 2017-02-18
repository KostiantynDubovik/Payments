package com.payments.web.servlets;

import com.payments.dto.LoginBean;
import com.payments.model.User;
import com.payments.model.enums.UserRole;
import com.payments.service.impl.UserServiceImpl;
import com.payments.utilites.PasswordAuthentication;
import com.payments.utilites.Validator;

import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.*;

/**
 * Servlet implementation class LoginServlet
 */
public class LoginServlet extends HttpServlet {
    public static final String PUT_ATTRIBUTE = "put attribute";
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    private static final UserServiceImpl userService = UserServiceImpl.getInstance();
    private static final String JSP_REFERENCE = "login.jsp";
    private static final String ERROR_MESSAGES = "error_messages";
    private static final String NOT_EXIST_USER_OR_WRONG_PASS = "not_exist_user_or_wrong_pass";
    private static final String LOGIN_MESSAGE_WRONG_PASS_OR_ACC = "login.message.wrong_pass_or_acc";
    private static final String USER_ROLE = "user_role";
    private static final String USER_LOGIN = "user_login";
    private static final String USER = "user";
    private static final String LOGIN = "login";
    private static final String PASSWORD = "password";
    private static final String JCAPTCHA = "jcaptcha";
    private static final String DNS_SECURITY_CODE = "dns_security_code";
    private static final String USER_LINK = "/user";
    private static final String ADMIN_LINK = "/admin";
    private static final String LOGIN_LINK = "/login";

    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest, HttpServletResponse)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(JSP_REFERENCE).forward(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest, HttpServletResponse)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LoginBean loginBean = extractLoginBeanFromRequest(request);

        HttpSession session = request.getSession();

        session.removeAttribute(ERROR_MESSAGES);

        Map<String, String> errorMessages = Validator.validateLogin(loginBean);

        if (errorMessages.isEmpty()) {
            User user = userService.getUser(loginBean.getLogin());
            if (user != null && PasswordAuthentication.validatePassword(loginBean.getPassword(), user.getPassword())) {
                putAttributesToSession(session, user);
                sendRedirectByRole(response, user.getRole());
                return;
            } else {
                errorMessages.put(NOT_EXIST_USER_OR_WRONG_PASS, LOGIN_MESSAGE_WRONG_PASS_OR_ACC);
            }
        }
        session.setAttribute(ERROR_MESSAGES, errorMessages);
        response.sendRedirect(LOGIN_LINK);

    }

    private void sendRedirectByRole(HttpServletResponse response, UserRole role) throws IOException {
        if (UserRole.ADMIN.equals(role)) {
            response.sendRedirect(ADMIN_LINK);

        } else {
            response.sendRedirect(USER_LINK);
        }
    }

    private void putAttributesToSession(HttpSession session, User user) {
        session.setAttribute(USER_ROLE, user.getRole());
        session.setAttribute(USER_LOGIN, user.getLogin());
        session.setAttribute(USER, user);
    }

    private LoginBean extractLoginBeanFromRequest(HttpServletRequest request) {
        LoginBean loginBean = new LoginBean();
        loginBean.setLogin(request.getParameter(LOGIN));
        loginBean.setPassword(request.getParameter(PASSWORD));
        loginBean.setRetrievedCaptcha(request.getParameter(JCAPTCHA));
        loginBean.setGeneratedCaptcha((String) request.getSession(false).getAttribute(DNS_SECURITY_CODE));
        return loginBean;
    }
}