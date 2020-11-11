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

public class LoanInCmd extends AbstractCmd {
    private static final Logger log = Logger.getLogger(LoanInCmd.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        log.debug("LoanInCmd starts");

        String loanId = request.getParameter(ContextParam.LOAN_ID_TO_PROCESS);

        if (LoanDao.loanIn(
                Integer.parseInt(loanId),
                LocalDate.now())
        ) {
            log.debug("LoanInCmd finished");
            return Path.getCurrentPageName(request);
        }

        log.debug("LoanInCmd finished with error");
        return getErrorPageWithMessage(request,
                "Error of returning a book loan, your request was not completed, try again, please.");
    }
}
