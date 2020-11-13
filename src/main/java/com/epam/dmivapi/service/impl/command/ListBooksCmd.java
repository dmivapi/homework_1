package com.epam.dmivapi.service.impl.command;

import com.epam.dmivapi.repository.impl.BookRepositoryImpl;
import com.epam.dmivapi.entity.Book;
import com.epam.dmivapi.ContextParam;
import com.epam.dmivapi.Path;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

public class ListBooksCmd extends ListWithPaginationCmd {
    private static final Logger log = Logger.getLogger(ListBooksCmd.class);

    @Override
    protected boolean getListToSession(HttpServletRequest request, int currentPage, int recordsPerPage) {
        String title = request.getParameter(ContextParam.BK_TITLE);
        String author = request.getParameter(ContextParam.BK_AUTHOR);
        String orderBy = request.getParameter(ContextParam.BS_ORDER_BY);
        String orderByDir = request.getParameter(ContextParam.BS_ORDER_BY_DIRECTION);
        boolean isAscOrder = !"DESC".equalsIgnoreCase(orderByDir);

        String languageCode = ContextParam.getCurrentLocale(request.getSession());
        log.trace("Got default language code --> " + languageCode);

        List<Book> books = BookRepositoryImpl.findBooks(languageCode,
                title,
                author,
                orderBy, isAscOrder,
                currentPage, recordsPerPage
        );

        request.setAttribute(ContextParam.BS_BOOKS, books);
        log.trace("Set the request attribute: books --> " + books);

        return books != null;
    }

    @Override
    protected int getNumberOfRows(HttpServletRequest request) {
        HttpSession session = request.getSession();
        return BookRepositoryImpl.countBooks(ContextParam.getCurrentLocale(session),
                request.getParameter(ContextParam.BK_TITLE),
                request.getParameter(ContextParam.BK_AUTHOR)

        );
    }

    @Override
    protected String getForwardPage() {
        return Path.PAGE__LIST_BOOKS;
    }
}
