package com.payments.web.servlets.admin;

import com.payments.model.Bill;
import com.payments.service.impl.BillServiceImpl;
import com.payments.web.servlets.ActionChoose;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet implementation class DoPaymentServlet
 */
public class ManageBillServlet extends HttpServlet implements ActionChoose {


    /**
     *
     */
    private static final long serialVersionUID = 1870504758779766248L;

    private static final BillServiceImpl BILL_SERVICE = BillServiceImpl.getInstance();

    private static final String JSP_REFERENCE = "/WEB-INF/admin/manageBill.jsp";
    private static final String ADMIN_BILLS_BILL_NUMBER = "/admin/bills/bill?number=";
    private static final String NUMBER = "number";
    private static final String BILL = "bill";
    private static final String BLOCK = "b";
    private static final String UNBLOCK = "u";

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String billNumber = request.getParameter(NUMBER);
        if (billNumber != null) {
            request.setAttribute(BILL, BILL_SERVICE.getBillByNumber(billNumber));
        }
        request.getRequestDispatcher(JSP_REFERENCE).forward(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] numberAndAction = getNumberAndAction(request);
        String billNumber = numberAndAction[0];
        Bill bill = BILL_SERVICE.getBillByNumber(billNumber);
        String action = numberAndAction[1];
        if (action.equals(BLOCK)) {
            bill.setBlocked(true);
        } else if (action.equals(UNBLOCK)) {
            bill.setBlocked(false);
            bill.setNeedToUnblock(false);
        }
        BILL_SERVICE.update(bill);
        response.sendRedirect(ADMIN_BILLS_BILL_NUMBER.concat(billNumber));
    }


}
