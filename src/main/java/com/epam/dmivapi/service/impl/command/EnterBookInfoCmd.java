package com.epam.dmivapi.service.impl.command;

import com.epam.dmivapi.repository.impl.BookRepositoryImpl;
import com.epam.dmivapi.ContextParam;
import com.epam.dmivapi.Path;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class EnterBookInfoCmd extends AbstractCmd {
    private static final Logger log = Logger.getLogger(EnterBookInfoCmd.class);

    @Override
    public String execute(HttpServletRequest request) throws IOException, ServletException {
        log.debug("EnterBookInfoCmd starts");

        request.setAttribute(ContextParam.BK_AUTHORS, BookRepositoryImpl.findAuthors());
        request.setAttribute(ContextParam.BK_PUBLISHERS, BookRepositoryImpl.findPublishers());
        request.setAttribute(ContextParam.BK_GENRES, BookRepositoryImpl.findGenres(
                ContextParam.getCurrentLocale(request.getSession()))
        );

        log.debug("EnterBookInfoCmd finished");
        return Path.PAGE__ENTER_BOOK_INFO;
    }
}
