package com.payments.web.servlets.admin;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class AdministrationServlet
 */
public class AdministrationServlet extends HttpServlet {

	private static final String JSP_REFERENCE = "/WEB-INF/user/adminPage.jsp";

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher(JSP_REFERENCE).forward(request, response);
	}


}
