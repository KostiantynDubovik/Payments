package com.payments.web.servlets.user;

import com.payments.dto.PaymentBean;
import com.payments.model.OutgoingPayment;
import com.payments.model.User;
import com.payments.model.enums.PaymentStatus;
import com.payments.service.api.OutgoingPaymentService;
import com.payments.service.impl.OutgoingPaymentServiceImpl;
import com.payments.utilites.ValidatePayment;
import org.apache.commons.lang3.math.NumberUtils;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

/**
 * Servlet implementation class DoPaymentServlet
 */
public class ViewOutgoingPaymentServlet extends HttpServlet {


    private static final OutgoingPaymentService OUTGOING_PAYMENT_SERVICE = OutgoingPaymentServiceImpl.getInstance();


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");

        if (NumberUtils.isDigits(id)) {
            int parsedId = Integer.parseInt(id);
            OutgoingPayment outgoingPayment = OUTGOING_PAYMENT_SERVICE.getById(parsedId);
            request.setAttribute("outgoing_payment", outgoingPayment);
        }
        request.getRequestDispatcher("/WEB-INF/user/viewOutgoingPayment.jsp").forward(request, response);
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        session.removeAttribute("error_messages");
        String billFrom = request.getParameter("from");
        String billTo = request.getParameter("to");
        OutgoingPayment outgoingPayment = OUTGOING_PAYMENT_SERVICE.getOutgoingPaymentByBillNumbers(billFrom, billTo);
        if (outgoingPayment == null) {
            response.sendRedirect("/user/outgoing_payment?from=" + billFrom + "&to=" + billTo);
            return;
        }
        outgoingPayment.setPaymentStatus(PaymentStatus.SENT);
        outgoingPayment.setDateOfPayment(new Date(System.currentTimeMillis()));
        Map<String, String> errorMessages = ValidatePayment.validatePayment(createPaymentBean(outgoingPayment, session));
        if (errorMessages.isEmpty()) {
            outgoingPayment.setDateOfPayment(new Date(System.currentTimeMillis()));
            OUTGOING_PAYMENT_SERVICE.update(outgoingPayment);
            response.sendRedirect("/user/outgoing_payment?from=" + billFrom + "&to=" + billTo);
        } else {
            session.setAttribute("error_messages", errorMessages);
            response.sendRedirect("/user/outgoing_payment?from="+outgoingPayment.getBillWhereFromIsPayment().getBillNumber()+"&to="+outgoingPayment.getNumberOfBillWherePaymentGoing());
        }
    }

    private PaymentBean createPaymentBean(OutgoingPayment outgoingPayment, HttpSession session) {
        PaymentBean paymentBean = new PaymentBean();
        paymentBean.setComment(outgoingPayment.getComment());
        paymentBean.setSumOfPayment(outgoingPayment.getSumOfPayment());
        paymentBean.setCommission(outgoingPayment.getCommission());
        paymentBean.setUserBills(((User)session.getAttribute("user")).getBills());
        paymentBean.setBillToNumber(outgoingPayment.getNumberOfBillWherePaymentGoing());
        paymentBean.setBillFromNumber(outgoingPayment.getBillWhereFromIsPayment().getBillNumber());
        paymentBean.setFrom(true);
        return paymentBean;
    }

}
