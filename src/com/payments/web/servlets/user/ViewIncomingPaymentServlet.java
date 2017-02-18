package com.payments.web.servlets.user;

import com.payments.model.IncomingPayment;
import com.payments.service.api.IncomingPaymentService;
import com.payments.service.impl.IncomingPaymentServiceImpl;
import org.apache.commons.lang3.math.NumberUtils;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet implementation class DoPaymentServlet
 */
public class ViewIncomingPaymentServlet extends HttpServlet {


    private static final IncomingPaymentService INCOMING_PAYMENT_SERVICE = IncomingPaymentServiceImpl.getInstance();

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        if (NumberUtils.isDigits(id)) {
            int parsedId = Integer.parseInt(id);
            IncomingPayment incomingPayment = INCOMING_PAYMENT_SERVICE.getById(parsedId);
            request.setAttribute("incoming_payment", incomingPayment);
        }
        request.getRequestDispatcher("/WEB-INF/user/viewIncomingPayment.jsp").forward(request, response);
    }
}
