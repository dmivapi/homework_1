package com.epam.dmivapi.service.impl.command;

import com.epam.dmivapi.bean.LoanBean;
import com.epam.dmivapi.repository.impl.LoanRepositoryImpl;
import com.epam.dmivapi.ContextParam;
import com.epam.dmivapi.Path;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

public class ListLoansOfSelfCmd extends ListWithPaginationCmd {
    private static final Logger log = Logger.getLogger(ListLoansOfSelfCmd.class);

    @Override
    protected boolean getListToSession(HttpServletRequest request, int currentPage, int recordsPerPage) {
        HttpSession session = request.getSession();

        List<LoanBean> loans = LoanRepositoryImpl.findLoans(null,
                ContextParam.getCurrentUser(session).getId(),
                ContextParam.getCurrentLocale(session),
                currentPage,
                recordsPerPage
        );

        request.setAttribute(ContextParam.LS_LOANS, loans);
        log.trace("Set the request attribute: loans --> " + loans);
        return loans != null;
    }

    @Override
    protected int getNumberOfRows(HttpServletRequest request) {
        HttpSession session = request.getSession();
        return LoanRepositoryImpl.countLoans(null,
                ContextParam.getCurrentUser(session).getId(),
                ContextParam.getCurrentLocale(session));
    }

    @Override
    protected String getForwardPage() {
        return Path.PAGE__LIST_LOANS_SINGLE_USER;
    }
}
