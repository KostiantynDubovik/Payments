package com.payments.web.filters;

import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author Kostiantyn Dubovik
 */
public class CharsetFilter implements Filter {
    private static final Logger LOG = Logger.getLogger(CharsetFilter.class);

    private static final String CONTENT_TYPE = "text/html; charset=UTF-8";
    private static final String ENCODING = "UTF-8";
    private static final String REQUEST_ENCODING = "encoding";
    private static final String FILTER_INITIALIZATION_STARTS = "Filter initialization starts";
    private static final String ENCODING_FROM_WEB_XML = "Encoding from web.xml --> ";
    private static final String FILTER_INITIALIZATION_FINISHED = "Filter initialization finished";
    private static final String FILTER_DESTRUCTION_STARTS = "Filter destruction starts";
    private static final String FILTER_DESTRUCTION_FINISHED = "Filter destruction finished";
    /**
     * Encoding of web pages
     */
    private String encoding;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LOG.debug(FILTER_INITIALIZATION_STARTS);
        encoding = filterConfig.getInitParameter(REQUEST_ENCODING);
        if (encoding == null)
            encoding = ENCODING;
        LOG.trace(ENCODING_FROM_WEB_XML + encoding);
        LOG.debug(FILTER_INITIALIZATION_FINISHED);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        if (null == servletRequest.getCharacterEncoding()){
            servletRequest.setCharacterEncoding(encoding);
        }
        servletResponse.setContentType(CONTENT_TYPE);
        servletResponse.setCharacterEncoding(encoding);
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        LOG.debug(FILTER_DESTRUCTION_STARTS);
        // no op
        LOG.debug(FILTER_DESTRUCTION_FINISHED);
    }
}