package com.payments.web.servlets.user;

import com.payments.dto.PaymentBean;
import com.payments.model.IncomingPayment;
import com.payments.model.User;
import com.payments.model.enums.PaymentStatus;
import com.payments.service.api.IncomingPaymentService;
import com.payments.service.impl.IncomingPaymentServiceImpl;
import com.payments.service.impl.UserServiceImpl;
import com.payments.utilites.ValidatePayment;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * @author Kostiantyn Dubovik
 */
public class RefillBillServlet extends HttpServlet {
    private static final String JSP_REFERENCE = "/WEB-INF/user/refill.jsp";

    private static final IncomingPaymentService INCOMING_PAYMENT_SERVICE = IncomingPaymentServiceImpl.getInstance();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(JSP_REFERENCE).forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        session.removeAttribute("error_messages");
        session.setAttribute("user", UserServiceImpl.getInstance().getUser(((User) session.getAttribute("user")).getLogin()));
        PaymentBean paymentBean = extractPaymentBeanFromRequest(request);
        Map<String, String> errorMessages = ValidatePayment.validateRefill(paymentBean);

        if (errorMessages.isEmpty()) {
            INCOMING_PAYMENT_SERVICE.insert(extractIncomingPaymentFromPaymentBean(paymentBean));
            response.sendRedirect("/user/bills/bill?number=" + paymentBean.getBill().getBillNumber());
        } else {
            session.setAttribute("error_messages", errorMessages);
            response.sendRedirect("/user/refill");
        }

    }

    private PaymentBean extractPaymentBeanFromRequest(HttpServletRequest request) {
        PaymentBean paymentBean = new PaymentBean();
        paymentBean.setSumOfPayment(new BigDecimal(request.getParameter("sum")).setScale(2, BigDecimal.ROUND_HALF_UP));
        paymentBean.setUserBills(((User) request.getSession().getAttribute("user")).getBills());
        paymentBean.setCommission(ValidatePayment.getCommission(paymentBean.getSumOfPayment()));
        paymentBean.setComment(String.format("Refill of bill %s", paymentBean.getBillToNumber()));
        paymentBean.setFrom(false);
        paymentBean.setBillToNumber(request.getParameter("bill_to_refill_number"));
        return paymentBean;
    }

    private IncomingPayment extractIncomingPaymentFromPaymentBean(PaymentBean paymentBean) {
        IncomingPayment incomingPayment = new IncomingPayment();
        incomingPayment.setPaymentFromBill("Refill payment");
        incomingPayment.setPaymentToBill(paymentBean.getBill());
        incomingPayment.setCommission(paymentBean.getCommission());
        incomingPayment.setSumOfPayment(paymentBean.getSumOfPayment().subtract(incomingPayment.getCommission()));
        incomingPayment.setComment(paymentBean.getComment());
        incomingPayment.setPaymentStatus(PaymentStatus.SENT);
        incomingPayment.setDateOfPayment(new Date(System.currentTimeMillis()));
        return incomingPayment;

    }
}
