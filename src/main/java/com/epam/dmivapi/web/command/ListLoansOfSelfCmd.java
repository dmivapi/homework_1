package com.epam.dmivapi.web.command;

import com.epam.dmivapi.db.bean.LoanBean;
import com.epam.dmivapi.db.dao.BookDao;
import com.epam.dmivapi.db.dao.LoanDao;
import com.epam.dmivapi.db.entity.User;
import com.epam.dmivapi.web.ContextParam;
import com.epam.dmivapi.web.Path;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class ListLoansOfSelfCmd extends ListWithPaginationCmd {
    private static final Logger log = Logger.getLogger(ListLoansOfSelfCmd.class);

    @Override
    protected boolean getListToSession(HttpServletRequest request, int currentPage, int recordsPerPage) {
        HttpSession session = request.getSession();

        List<LoanBean> loans = LoanDao.findLoans(null,
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
        return LoanDao.countLoans(null,
                ContextParam.getCurrentUser(session).getId(),
                ContextParam.getCurrentLocale(session));
    }

    @Override
    protected String getForwardPage() {
        return Path.PAGE__LIST_LOANS_SINGLE_USER;
    }
}
