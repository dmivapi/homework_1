package com.epam.dmivapi.web.command;

import com.epam.dmivapi.db.dao.BookDao;
import com.epam.dmivapi.web.ContextParam;
import com.epam.dmivapi.web.Path;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class EnterBookInfoCmd extends AbstractCmd {
    private static final Logger log = Logger.getLogger(EnterBookInfoCmd.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        log.debug("EnterBookInfoCmd starts");

        request.setAttribute(ContextParam.BK_AUTHORS, BookDao.findAuthors());
        request.setAttribute(ContextParam.BK_PUBLISHERS, BookDao.findPublishers());
        request.setAttribute(ContextParam.BK_GENRES, BookDao.findGenres(
                ContextParam.getCurrentLocale(request.getSession()))
        );

        log.debug("EnterBookInfoCmd finished");
        return Path.PAGE__ENTER_BOOK_INFO;
    }
}
