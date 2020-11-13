package com.epam.dmivapi.service.impl.command;

import com.epam.dmivapi.bean.LoanBean;
import com.epam.dmivapi.repository.impl.LoanRepositoryImpl;
import com.epam.dmivapi.ContextParam;
import com.epam.dmivapi.Path;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class ListLoansOfAllCmd extends ListWithPaginationCmd {
    private static final Logger log = Logger.getLogger(ListLoansOfAllCmd.class);

    @Override
    protected boolean getListToSession(HttpServletRequest request, int currentPage, int recordsPerPage) {
        List<LoanBean> loans = LoanRepositoryImpl.findLoans(null, null,
                ContextParam.getCurrentLocale(request.getSession()),
                currentPage,
                recordsPerPage
        );

        request.setAttribute(ContextParam.LS_LOANS, loans);
        log.trace("Set the request attribute: loans --> " + loans);
        return loans != null;
    }

    @Override
    protected int getNumberOfRows(HttpServletRequest request) {
        return LoanRepositoryImpl.countLoans(null, null,
                ContextParam.getCurrentLocale(request.getSession()));
    }

    @Override
    protected String getForwardPage() {
        return Path.PAGE__LIST_LOANS_MULTIPLE_USERS;
    }
}
