package com.epam.dmivapi.filter;

import com.epam.dmivapi.dto.Role;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.epam.dmivapi.ContextParam.getCurrentUserRole;

public class PageDirectAccessFilter implements Filter {
    private static final Logger log = Logger.getLogger(PageDirectAccessFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        Role userRole = getCurrentUserRole(httpRequest.getSession());

        String redirectPage = httpRequest.getContextPath() + userRole.getDefaultPage(); // TODO what in the world?...
        log.debug("PageDirectAccessFilter redirecting to --> " + redirectPage);
        httpResponse.sendRedirect(redirectPage);
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
