package com.payments.web.servlets.admin;

import com.payments.model.Bill;
import com.payments.model.User;
import com.payments.service.api.BillService;
import com.payments.service.impl.BillServiceImpl;
import com.payments.utilites.Pages;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ManageOrders
 */
public class ManageBillsServlet extends HttpServlet {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /**
     *
     */
    private static final BillService BILL_SERVICE = BillServiceImpl.getInstance();

    private static final String JSP_REFERENCE = "/WEB-INF/admin/manageBills.jsp";

    private static final String DEFAULT_ORDER_BY = "number";
    private static final String ATTRIBUTE_NAME = "bills_list";
    private static final String USER_ID = "user_id";
    private static final int GET_ALL_BILLS = 0;

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userIdParameter = request.getParameter(USER_ID);
        if (userIdParameter != null) {
            int userId = Integer.parseInt(userIdParameter);
            Pages.doPagination(request, BILL_SERVICE, userId, DEFAULT_ORDER_BY, ATTRIBUTE_NAME);
        } else {
            Pages.doPagination(request, BILL_SERVICE, GET_ALL_BILLS, DEFAULT_ORDER_BY, ATTRIBUTE_NAME);
        }
        request.getRequestDispatcher(JSP_REFERENCE).forward(request, response);
    }
}
