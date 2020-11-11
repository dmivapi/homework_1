package com.epam.dmivapi.web.command;

import com.epam.dmivapi.db.dao.UserDao;
import com.epam.dmivapi.db.entity.User;
import com.epam.dmivapi.web.ContextParam;
import com.epam.dmivapi.web.Path;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * This class defines a template method execute() to provide
 * common behaviour of list with pagination
 */
public abstract class ListWithPaginationCmd extends AbstractCmd {

    protected abstract boolean getListToSession(HttpServletRequest request, int currentPage, int recordsPerPage);
    protected abstract int getNumberOfRows(HttpServletRequest request);
    protected abstract String getForwardPage();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        // for pagination purposes
        int currentPage = ContextParam.getPgnCurrentPage(request);
        int recordsPerPage = ContextParam.getPgnRecordsPerPage(request);

        // to be overridden in descendants (main work of filling the list to display)
        if (!getListToSession(request, currentPage, recordsPerPage))
            return Path.PAGE__ERROR; // TODO consider changing

        // for pagination work part (2)
        int rows = getNumberOfRows(request); // to be overridden in descendants
        int nOfPages = rows / recordsPerPage;
        if (rows % recordsPerPage > 0)
            nOfPages++;

        ContextParam.setPgnNumberOfPages(request, nOfPages);
//        ContextParam.setPgnCurrentPage(request, currentPage);
        ContextParam.setPgnRecordsPerPage(request, recordsPerPage);

        return getForwardPage(); // to be overridden in descendants
    }
}
