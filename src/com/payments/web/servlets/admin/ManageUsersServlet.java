package com.payments.web.servlets.admin;

import com.payments.model.enums.UserRole;
import com.payments.service.api.UserService;
import com.payments.service.impl.UserServiceImpl;
import com.payments.utilites.Pages;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ManageUsersServlet
 */
public class ManageUsersServlet extends HttpServlet {
    /**
     *
     */
    private static final long serialVersionUID = 2960672498859549101L;

    private static final UserService USER_SERVICE = UserServiceImpl.getInstance();

    private static final String JSP_REFERENCE = "/WEB-INF/admin/manageUsers.jsp";

    private static final String DEFAULT_ORDER_BY = "firstname";
    private static final String ACTION = "action";
    private static final String BLOCK = "block";
    private static final String UNBLOCK = "unblock";
    private static final String USER_LOGIN = "user_login";
    private static final String ADMIN_USERS_LINK = "/admin/users";

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Pages.doPagination(request,USER_SERVICE, UserRole.USER.getRoleId(), DEFAULT_ORDER_BY, "users");
        request.getRequestDispatcher(JSP_REFERENCE).forward(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(request.getParameter(ACTION).equals(BLOCK)){
            USER_SERVICE.setBlocked(request.getParameter(USER_LOGIN));
        } else if (request.getParameter(ACTION).equals(UNBLOCK)){
            USER_SERVICE.setUnblocked(request.getParameter(USER_LOGIN));
        }
        response.sendRedirect(ADMIN_USERS_LINK);
    }

}
