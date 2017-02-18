package com.payments.web.servlets.admin;

import com.payments.model.enums.UserRole;
import com.payments.service.api.UserService;
import com.payments.service.impl.UserServiceImpl;
import com.payments.utilites.Pages;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Kostiantyn Dubovik
 */
public class ViewAllUsersMoneyServlet extends HttpServlet {

    private static final UserService USER_SERVICE = UserServiceImpl.getInstance();

    private static final String JSP_REFERENCE = "/WEB-INF/admin/viewAllUsersMoney.jsp";

    private static final String DEFAULT_ORDER_BY = "login";
    private static final String ATTRIBUTE_NAME = "users";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Pages.doPagination(request,USER_SERVICE, UserRole.USER.getRoleId(), DEFAULT_ORDER_BY, ATTRIBUTE_NAME);
        request.getRequestDispatcher(JSP_REFERENCE).forward(request, response);
    }
}
