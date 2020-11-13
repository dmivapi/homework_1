package com.epam.dmivapi.filter;

import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class EncodingFilter implements Filter {
    private static final Logger log = Logger.getLogger(EncodingFilter.class);

    private String encoding;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.debug("Filter starts");

        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        log.trace("Request uri --> " + httpRequest.getRequestURI());

        if (encoding != null && !encoding.equalsIgnoreCase(servletRequest.getCharacterEncoding())) {
            log.trace("Request encoding = null, set encoding --> " + encoding);
            servletRequest.setCharacterEncoding(encoding);
            servletResponse.setCharacterEncoding(encoding);
        }

        log.debug("Filter finished");
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.debug("Filter initialization starts");
        encoding = filterConfig.getInitParameter("encoding");
        log.trace("Encoding from web.xml --> " + encoding);
        log.debug("Filter initialization finished");
    }

    @Override
    public void destroy() {
        log.debug("Filter destruction starts");

        encoding = null;

        log.debug("Filter destruction finished");
    }
}
