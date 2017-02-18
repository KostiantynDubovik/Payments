package com.payments.web.servlets.user;


import com.payments.dto.PaymentBean;
import com.payments.model.OutgoingPayment;
import com.payments.model.User;
import com.payments.service.api.OutgoingPaymentService;
import com.payments.service.impl.OutgoingPaymentServiceImpl;
import com.payments.service.impl.UserServiceImpl;
import com.payments.utilites.ValidatePayment;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class DoPaymentServlet
 */
public class DoPaymentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String JSP_REFERENCE = "/WEB-INF/user/doPayment.jsp";

    private static final OutgoingPaymentService OUTGOING_PAYMENT_SERVICE = OutgoingPaymentServiceImpl.getInstance();


    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String billNumber = request.getParameter("bill");
        if (billNumber != null) {
            request.setAttribute("billNumber", billNumber);
        }
        request.getRequestDispatcher(JSP_REFERENCE).forward(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        session.removeAttribute("error_messages");
        session.setAttribute("user", UserServiceImpl.getInstance().getUser(((User) session.getAttribute("user")).getLogin()));
        PaymentBean paymentBean = createPaymentBean(request);
        Map<String,String> errorMessages = ValidatePayment.validatePayment(paymentBean);
        if(errorMessages.isEmpty()){
            OUTGOING_PAYMENT_SERVICE.insert(extractOutgoingPaymentFromPaymentBean(paymentBean));
            response.sendRedirect("/user/outgoing_payment?from="+paymentBean.getBillFromNumber()+"&to="+paymentBean.getBillToNumber());
        }
        else {
            session.setAttribute("error_messages", errorMessages);
            response.sendRedirect("/user/pay");
        }
    }

    private static PaymentBean createPaymentBean(HttpServletRequest request) {
        PaymentBean paymentBean = new PaymentBean();
        HttpSession session = request.getSession();
        paymentBean.setUserBills(((User) session.getAttribute("user")).getBills());
        paymentBean.setBillFromNumber(request.getParameter("bill_from_number"));
        paymentBean.setBillToNumber(request.getParameter("bill_to_number"));
        paymentBean.setSumOfPayment(new BigDecimal(request.getParameter("sum")).setScale(2, BigDecimal.ROUND_HALF_UP));
        paymentBean.setCommission(ValidatePayment.getCommission(new BigDecimal(request.getParameter("sum"))));
        paymentBean.setComment(request.getParameter("comment"));
        paymentBean.setFrom(true);
        return paymentBean;
    }

    private OutgoingPayment extractOutgoingPaymentFromPaymentBean(PaymentBean paymentBean){
        OutgoingPayment outgoingPayment = new OutgoingPayment();
        outgoingPayment.setNumberOfBillWherePaymentGoing(paymentBean.getBillToNumber());
        outgoingPayment.setPaymentFromBill(paymentBean.getBill());
        outgoingPayment.setSumOfPayment(paymentBean.getSumOfPayment());
        outgoingPayment.setComment(paymentBean.getComment());
        outgoingPayment.setCommission(ValidatePayment.getCommission(outgoingPayment.getSumOfPayment()));
        return outgoingPayment;
    }
}
