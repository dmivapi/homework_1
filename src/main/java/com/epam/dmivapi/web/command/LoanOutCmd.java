package com.epam.dmivapi.web.command;

import com.epam.dmivapi.db.dao.LoanDao;
import com.epam.dmivapi.web.ContextParam;
import com.epam.dmivapi.web.Path;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;

import static com.epam.dmivapi.web.ContextParam.getErrorPageWithMessage;

public class LoanOutCmd extends AbstractCmd {
    private static final Logger log = Logger.getLogger(LoanOutCmd.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        log.debug("LoanOutCmd starts");

        int defaultLoanTerm = (Integer)request.getServletContext().getAttribute(ContextParam.DEFAULT_LOAN_TERM_IN_DAYS);

        if (LoanDao.loanOut(
                Integer.parseInt((String)request.getParameter(ContextParam.LOAN_ID_TO_PROCESS)),
                LocalDate.now(),
                LocalDate.now().plusDays(defaultLoanTerm))
            ) {
            log.debug("LoanOutCmd finished");
            return Path.getCurrentPageName(request);
        }

        log.debug("LoanOutCmd finished with error");
        return getErrorPageWithMessage(request,
                "Error of giving out a book loan, your request was not completed, try again, please.");
    }
}
