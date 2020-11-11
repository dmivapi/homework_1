package com.epam.dmivapi.web.command;

import com.epam.dmivapi.db.dao.BookDao;
import com.epam.dmivapi.db.dao.LoanDao;
import com.epam.dmivapi.web.ContextParam;
import com.epam.dmivapi.web.Path;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

import static com.epam.dmivapi.web.ContextParam.getErrorPageWithMessage;

public class BookRemoveCmd extends AbstractCmd {
    private static final Logger log = Logger.getLogger(BookRemoveCmd.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        log.debug("BookRemoveCmd starts");
        String[] publicationIds = request.getParameterValues(ContextParam.PUBLICATIONS_IDS_TO_PROCESS);

        if (BookDao.bookRemove(
                Arrays.stream(publicationIds).mapToInt(Integer::parseInt).toArray())
        ) {
            log.debug("BookRemoveCmd finished");
            return Path.getCurrentPageName(request);
        }

        log.debug("BookRemoveCmd finished with error");
        return getErrorPageWithMessage(request,
                "Error of removing books, your request was not completed, try again, please.");

    }
}
