package com.epam.dmivapi.web.command;

import com.epam.dmivapi.db.dao.UserDao;
import com.epam.dmivapi.web.ContextParam;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.epam.dmivapi.web.ContextParam.getErrorPageWithMessage;

public class UserRemoveCmd extends AbstractCmd {
    private static final Logger log = Logger.getLogger(UserRemoveCmd.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        log.debug("UserRemoveCmd starts");

        String userId = request.getParameter(ContextParam.USER_ID_TO_PROCESS);

        if (UserDao.removeUser(Integer.parseInt(userId))) {
            log.debug("UserRemoveCmd finished");
            return Command.LIST_USERS_LIBRARIANS.getPath();
        }

        log.debug("UserRemoveCmd finished with error");
        return getErrorPageWithMessage(request,
                "Error of removing librarian, your request was not completed, try again, please.");

    }
}
