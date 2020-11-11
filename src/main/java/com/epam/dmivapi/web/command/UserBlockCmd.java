package com.epam.dmivapi.web.command;

import com.epam.dmivapi.db.dao.UserDao;
import com.epam.dmivapi.web.ContextParam;
import com.epam.dmivapi.web.Path;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.epam.dmivapi.web.ContextParam.getErrorPageWithMessage;

public class UserBlockCmd extends AbstractCmd {
    private static final Logger log = Logger.getLogger(UserBlockCmd.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        log.debug("UserBlockCmd starts");

        String blockOption = request.getParameter(ContextParam.BLOCK_OPTION);
        String userId = request.getParameter(ContextParam.USER_ID_TO_PROCESS);

        if (UserDao.updateUserBlockStatus(
                Integer.parseInt(userId),
                Boolean.valueOf(blockOption))
        ) {
            log.debug("UserBlockCmd finished");
            return Path.getCurrentPageName(request);
        }

        log.debug("UserBlockCmd finished with error");
        return getErrorPageWithMessage(request,
                "Error of removing loans, your request was not completed, try again, please.");

    }
}
