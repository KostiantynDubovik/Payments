package com.payments.web.servlets.user;

import com.payments.model.User;
import com.payments.service.api.UserService;
import com.payments.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author Kostiantyn Dubovik
 */
public class ViewAllUserMoneyServlet extends HttpServlet {
    private static final String JSP_REFERENCE = "/WEB-INF/user/viewAllUserMoney.jsp";
    private static final UserService USER_SERVICE = UserServiceImpl.getInstance();
    public static final String USER = "user";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        session.setAttribute("user", USER_SERVICE.getUser(((User)session.getAttribute(USER)).getLogin()));
        request.getRequestDispatcher(JSP_REFERENCE).forward(request, response);
    }
}
