package com.epam.dmivapi.web.command;

import com.epam.dmivapi.db.dao.LoanDao;
import com.epam.dmivapi.web.ContextParam;
import com.epam.dmivapi.web.Path;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.epam.dmivapi.web.ContextParam.getErrorPageWithMessage;

public class LoanRemoveCmd extends AbstractCmd {
    private static final Logger log = Logger.getLogger(LoanRemoveCmd.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        log.debug("LoanRemoveCmd starts");

        if (LoanDao.loanRemove(
                Integer.parseInt((String)request.getParameter(ContextParam.LOAN_ID_TO_PROCESS)))
        ) {
            log.debug("LoanRemoveCmd finished");
            return Path.getCurrentPageName(request);
        }

        log.debug("LoanRemoveCmd finished with error");
        return getErrorPageWithMessage(request,
                "Error of removing loans, your request was not completed, try again, please.");

    }
}
