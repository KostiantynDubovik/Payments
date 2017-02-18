package com.payments.web.servlets.user;

import com.payments.model.Bill;
import com.payments.model.User;
import com.payments.service.api.BillService;
import com.payments.service.impl.BillServiceImpl;
import com.payments.web.servlets.ActionChoose;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Servlet implementation class DoPaymentServlet
 */
public class ViewBillServlet extends HttpServlet implements ActionChoose {


    /**
     *
     */
    private static final long serialVersionUID = 1870504758779766248L;

    private static final BillService BILL_SERVICE = BillServiceImpl.getInstance();

    private static final String JSP_REFERENCE = "/WEB-INF/user/viewBill.jsp";
    private static final String USER_BILLS_BILL_NUMBER = "/user/bills/bill?number=";
    private static final String NUMBER = "number";
    private static final String USER = "user";
    private static final String BILL = "bill";

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String billNumber = request.getParameter(NUMBER);
        if (billNumber != null) {
            User user = (User) request.getSession().getAttribute(USER);
            user.setBills(BILL_SERVICE.getAll(user.getId(), 0, NUMBER));
            Bill bill = null;
            for (Bill userBill : user.getBills()) {
                if (userBill.getBillNumber().equals(billNumber)) {
                    bill = userBill;
                }
            }
            request.setAttribute(BILL, bill);
        }
        request.getRequestDispatcher(JSP_REFERENCE).forward(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute(USER);
        user.setBills(BILL_SERVICE.getAll(user.getId(), 0, NUMBER));
        String[] numberAndAction = getNumberAndAction(request);
        String billNumber = numberAndAction[0];
        String action = numberAndAction[1];
        for (Bill bill : user.getBills()) {
            if (bill.getBillNumber().equals(billNumber)) {
                if (action.equals("b")) {
                    bill.setBlocked(true);
                } else if (action.equals("u")) {
                    bill.setNeedToUnblock(true);
                }
                BILL_SERVICE.update(bill);
            }
        }
        response.sendRedirect(USER_BILLS_BILL_NUMBER.concat(billNumber));
    }


}
