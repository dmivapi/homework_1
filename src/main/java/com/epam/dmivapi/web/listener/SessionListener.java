package com.epam.dmivapi.web.listener;

import com.epam.dmivapi.db.entity.User;
import com.epam.dmivapi.web.ContextParam;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionListener implements HttpSessionListener {
    private static final Logger log = Logger.getLogger(SessionListener.class);

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        log.debug("Session initialization starts");
        HttpSession session = se.getSession();

        ContextParam.setCurrentLocale(session,
                (String) session.getServletContext().getAttribute(ContextParam.DEFAULT_LOCALE));

        ContextParam.setCurrentUserRole(session, User.Role.GUEST);
        session.setAttribute(ContextParam.PGN_RECORDS_PER_PAGE, ContextParam.RECORDS_PER_PAGE);

        log.debug("Session initialization finished");
    }
}
