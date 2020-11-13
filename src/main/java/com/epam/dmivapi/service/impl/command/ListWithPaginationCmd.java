package com.epam.dmivapi.service.impl.command;

import com.epam.dmivapi.ContextParam;
import com.epam.dmivapi.Path;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * This class defines a template method execute() to provide
 * common behaviour of list with pagination
 */
public abstract class ListWithPaginationCmd extends AbstractCmd {

    protected abstract boolean getListToSession(HttpServletRequest request, int currentPage, int recordsPerPage);
    protected abstract int getNumberOfRows(HttpServletRequest request);
    protected abstract String getForwardPage();

    @Override
    public String execute(HttpServletRequest request) throws IOException, ServletException {
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
