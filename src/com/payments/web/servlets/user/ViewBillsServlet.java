package com.payments.web.servlets.user;

import com.payments.model.User;
import com.payments.service.api.BillService;
import com.payments.service.impl.BillServiceImpl;
import com.payments.utilites.Pages;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Kostiantyn Dubovik
 */
public class ViewBillsServlet extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = -2980923591146857536L;

    private static final BillService BILL_SERVICE = BillServiceImpl.getInstance();

    private static final String JSP_REFERENCE = "/WEB-INF/user/viewBills.jsp";

    private static final String DEFAULT_ORDER_BY = "number";

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, java.io.IOException {
        Pages.doPagination(request,BILL_SERVICE,((User)request.getSession().getAttribute("user")).getId(),DEFAULT_ORDER_BY, "bills");
        request.getRequestDispatcher(JSP_REFERENCE).forward(request,response);
	}

}
