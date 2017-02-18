package com.payments.web.listeners;

import com.payments.datasource.DataSource;
import com.payments.model.enums.UserRole;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;


import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @author Kostiantyn Dubovik
 */

public class ContextListener implements ServletContextListener {
    private static final Logger LOG = Logger.getLogger(ContextListener.class);
    private static final String SERVLET_CONTEXT_INITIALIZATION_STARTS = "Servlet context initialization starts";
    private static final String GUEST_ROLE = "guest_role";
    private static final String USER_ROLE = "user_role";
    private static final String ADMIN_ROLE = "admin_role";
    private static final String SERVLET_CONTEXT_INITIALIZATION_FINISHED = "Servlet context initialization finished";
    private static final String DATA_SOURCE_INITIALIZATION_STARTED = "DataSource initialization started";
    private static final String DATA_SOURCE_INITIALIZATION_FINISHED = "DataSource initialization finished";
    private static final String SERVLET_CONTEXT_DESTRUCTION_STARTS = "Servlet context destruction starts";
    private static final String SERVLET_CONTEXT_DESTRUCTION_FINISHED = "Servlet context destruction finished";
    private static final String LOG4_J_INITIALIZATION_STARTED = "Log4J initialization started";
    private static final String WEB_INF_LOG4J_PROPERTIES = "WEB-INF/log4j.properties";
    private static final String LOG4J_HAS_BEEN_INITIALIZED = "Log4j has been initialized";
    private static final String CANNOT_CONFIGURE_LOG4J = "Cannot configure Log4j";
    private static final String LOG4_J_INITIALIZATION_FINISHED = "Log4J initialization finished";

    public void contextInitialized(ServletContextEvent event) {
        LOG.trace(SERVLET_CONTEXT_INITIALIZATION_STARTS);

        ServletContext servletContext = event.getServletContext();
        initLog4J(servletContext);
        initDataSource();
        servletContext.setAttribute(GUEST_ROLE, UserRole.GUEST);
        servletContext.setAttribute(USER_ROLE, UserRole.USER);
        servletContext.setAttribute(ADMIN_ROLE, UserRole.ADMIN);
        LOG.trace(SERVLET_CONTEXT_INITIALIZATION_FINISHED);
    }

    private void initDataSource() {
        LOG.trace(DATA_SOURCE_INITIALIZATION_STARTED);
        DataSource.getInstance();
        LOG.trace(DATA_SOURCE_INITIALIZATION_FINISHED);
    }

    public void contextDestroyed(ServletContextEvent sce) {
        LOG.trace(SERVLET_CONTEXT_DESTRUCTION_STARTS);
        sce.getServletContext().
        // no op
        log(SERVLET_CONTEXT_DESTRUCTION_FINISHED);
    }

    /**
     * Initializes log4j framework.
     *
     //* @param servletContext s
     */
    private static void initLog4J(ServletContext servletContext) {
        LOG.trace(LOG4_J_INITIALIZATION_STARTED);
        try {
            PropertyConfigurator.configure(servletContext.getRealPath(WEB_INF_LOG4J_PROPERTIES));
            LOG.debug(LOG4J_HAS_BEEN_INITIALIZED);
        } catch (Exception ex) {
            LOG.error(CANNOT_CONFIGURE_LOG4J);
            ex.printStackTrace();
        }
        LOG.trace(LOG4_J_INITIALIZATION_FINISHED);
    }

}
