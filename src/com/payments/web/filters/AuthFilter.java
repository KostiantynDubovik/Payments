package com.payments.web.filters;

import com.payments.model.User;
import com.payments.model.enums.UserRole;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author Kostiantyn Dubovik
 */
public class AuthFilter implements Filter {
    /**
     *
     */
    private static final Logger LOG = Logger.getLogger(AuthFilter.class);
    private static final String USER_ROLE = "user_role";
    private static final String USER = "user";
    private static final String ADMIN_URI = "/admin";
    private static final String USER_URI = "/user";
    private static final String LOGIN_URI = "/login";
    private static final String REGISTER_URI = "/register";
    private static final String FILTER_DESTRUCTION_FINISHED = "Filter destruction finished";
    private static final String FILTER_DESTRUCTION_STARTS = "Filter destruction starts";
    private static final String WEB_INF_BLOCKED_JSP = "WEB-INF/blocked.jsp";
    private static final String FILTER_INITIALIZATION_STARTS = "Filter initialization starts";
    private static final String FILTER_INITIALIZATION_FINISHED = "Filter initialization finished";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LOG.debug(FILTER_INITIALIZATION_STARTS);
        // no op
        LOG.debug(FILTER_INITIALIZATION_FINISHED);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String uri = req.getRequestURI();

        HttpSession session = req.getSession();

        session.setMaxInactiveInterval(600);

        if (session.getAttribute(USER_ROLE) == null) {
            session.setAttribute(USER_ROLE, UserRole.GUEST);
        }

        boolean userExists = session.getAttribute(USER) != null;

        if (userExists) {
            if (((User) session.getAttribute(USER)).isBlocked())
                req.getRequestDispatcher(WEB_INF_BLOCKED_JSP).forward(req, res);
        }

        if (uri.startsWith(ADMIN_URI)) {
            if (UserRole.ADMIN.equals(session.getAttribute(USER_ROLE))) {
                chain.doFilter(request, response);
            } else if (UserRole.USER.equals(session.getAttribute(USER_ROLE))) {
                res.sendRedirect(USER_URI);
            }
        } else if (uri.startsWith(USER_URI)) {
            if (UserRole.USER.equals(session.getAttribute(USER_ROLE))
                    || UserRole.ADMIN.equals(session.getAttribute(USER_ROLE))) {
                chain.doFilter(request, response);
            } else {
                res.sendRedirect(LOGIN_URI);
            }
        } else if ((uri.startsWith(LOGIN_URI) || uri.startsWith(REGISTER_URI))
                && !UserRole.GUEST.equals(session.getAttribute(USER_ROLE))) {
            res.sendRedirect(USER_URI);
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        LOG.debug(FILTER_DESTRUCTION_STARTS);
        // no op
        LOG.debug(FILTER_DESTRUCTION_FINISHED);
    }
}
