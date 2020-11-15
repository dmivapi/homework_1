package com.epam.dmivapi.listener;

import com.epam.dmivapi.dto.Role;
import com.epam.dmivapi.ContextParam;
import org.apache.log4j.Logger;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@WebListener
public class SessionListener implements HttpSessionListener {
    private static final Logger log = Logger.getLogger(SessionListener.class);

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        log.debug("Session initialization starts");
        HttpSession session = se.getSession();

        ContextParam.setCurrentLocale(session,
                (String) session.getServletContext().getAttribute(ContextParam.DEFAULT_LOCALE));

        ContextParam.setCurrentUserRole(session, Role.GUEST);
        session.setAttribute(ContextParam.PGN_RECORDS_PER_PAGE, Integer.valueOf(ContextParam.RECORDS_PER_PAGE));

        log.debug("Session initialization finished");
    }
}
