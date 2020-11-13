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

public class LoanInCmd extends AbstractCmd {
    private static final Logger log = Logger.getLogger(LoanInCmd.class);

    @Override
    public String execute(HttpServletRequest request) throws IOException, ServletException {
        log.debug("LoanInCmd starts");

        String loanId = request.getParameter(ContextParam.LOAN_ID_TO_PROCESS);

        if (LoanRepositoryImpl.loanIn(
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
