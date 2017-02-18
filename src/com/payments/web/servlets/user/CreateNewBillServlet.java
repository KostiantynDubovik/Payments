package com.payments.web.servlets.user;

import com.payments.model.Bill;
import com.payments.model.User;
import com.payments.service.api.BillService;
import com.payments.service.impl.BillServiceImpl;
import com.payments.utilites.CreditCardNumberGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * @author Kostiantyn Dubovik
 */
public class CreateNewBillServlet extends HttpServlet {

    private static final BillService BILL_SERVICE = BillServiceImpl.getInstance();
    private static final String JSP_REFERENCE = "/WEB-INF/user/createNewBill.jsp";
    private static final String USER_BILLS_LINK = "/user/bills";
    private static final String BILL_NAME = "bill_name";
    private static final String USER = "user";


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(JSP_REFERENCE).forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BILL_SERVICE.insert(createBill(request));
        response.sendRedirect(USER_BILLS_LINK);
    }

    private Bill createBill(HttpServletRequest request) {
        Bill bill = new Bill();
        String billName = request.getParameter(BILL_NAME);
        if (billName == null) {
            billName = "";
        }
        bill.setBillName(billName);
        bill.setUserId(((User) request.getSession().getAttribute(USER)).getId());
        bill.setBillNumber(CreditCardNumberGenerator.getInstance().generate("5", 16));
        bill.setBlocked(false);
        bill.setNeedToUnblock(false);
        bill.setBalance(new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP));
        return bill;
    }
}
