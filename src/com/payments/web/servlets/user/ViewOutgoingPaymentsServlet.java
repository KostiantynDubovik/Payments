package com.payments.web.servlets.user;

import com.payments.model.User;
import com.payments.service.impl.OutgoingPaymentServiceImpl;
import com.payments.utilites.Pages;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet implementation class ViewIncomingPaymentsServlet
 */
public class ViewOutgoingPaymentsServlet extends HttpServlet {

    /**
     *
     */
    private static final long serialVersionUID = -9189003489011109010L;

    private static final OutgoingPaymentServiceImpl OUTGOING_PAYMENT_SERVICE = OutgoingPaymentServiceImpl.getInstance();

    private static final String DEFAULT_ORDER_BY = "date";

    private static final String JSP_REFERENCE = "/WEB-INF/user/viewOutgoingPayments.jsp";

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int userId = ((User) request.getSession().getAttribute("user")).getId();
        Pages.doPagination(request, OUTGOING_PAYMENT_SERVICE, userId, DEFAULT_ORDER_BY, "outgoing_payments");
        request.getRequestDispatcher(JSP_REFERENCE).forward(request, response);
    }



}
