package com.epam.dmivapi.service.impl.command;

import com.epam.dmivapi.repository.impl.LoanRepositoryImpl;
import com.epam.dmivapi.ContextParam;
import com.epam.dmivapi.Path;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDate;

import static com.epam.dmivapi.ContextParam.getErrorPageWithMessage;

public class LoanOutCmd extends AbstractCmd {
    private static final Logger log = Logger.getLogger(LoanOutCmd.class);

    @Override
    public String execute(HttpServletRequest request) throws IOException, ServletException {
        log.debug("LoanOutCmd starts");

        int defaultLoanTerm = (Integer)request.getServletContext().getAttribute(ContextParam.DEFAULT_LOAN_TERM_IN_DAYS);

        if (LoanRepositoryImpl.loanOut(
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
