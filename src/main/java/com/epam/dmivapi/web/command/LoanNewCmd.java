package com.epam.dmivapi.web.command;

import com.epam.dmivapi.db.dao.LoanDao;
import com.epam.dmivapi.db.entity.User;
import com.epam.dmivapi.web.ContextParam;
import com.epam.dmivapi.web.Path;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

import static com.epam.dmivapi.web.ContextParam.getErrorPageWithMessage;

public class LoanNewCmd extends AbstractCmd {
    private static final Logger log = Logger.getLogger(LoanNewCmd.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        log.debug("LoanNewCmd starts");

        User user = ContextParam.getCurrentUser(request.getSession());
        String[] publicationIds = request.getParameterValues(ContextParam.PUBLICATIONS_IDS_TO_PROCESS);

        if (user.isBlocked()) {
            log.trace("LoanNewCmd failed to create a loan request: user: "
                    + user.getLastName() + " " + user.getFirstName() + "is blocked");
            return getErrorPageWithMessage(request,
                    "Failed to make a loan request, administrator blocked your access to the library");
        }


        if (publicationIds == null) { // if this is a second attempt, then request parameter was saved to the session
            publicationIds = (String[]) request.getSession().getAttribute(ContextParam.PUBLICATIONS_IDS_TO_PROCESS);
        }

        if (LoanDao.loanNew(
                user.getId(),
                Arrays.stream(publicationIds).mapToInt(Integer::parseInt).toArray()
        )) {
            log.debug("LoanNewCmd finished");
            return Path.getCurrentPageName(request);
        }

        log.debug("LoanNewCmd finished with error");
        return getErrorPageWithMessage(request,
                "Error of making a loan, your request was not completed, try again, please.");
    }
}
