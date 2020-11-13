package com.epam.dmivapi.service.impl.command;

import com.epam.dmivapi.repository.impl.UserRepositoryImpl;
import com.epam.dmivapi.ContextParam;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static com.epam.dmivapi.ContextParam.getErrorPageWithMessage;

public class UserRemoveCmd extends AbstractCmd {
    private static final Logger log = Logger.getLogger(UserRemoveCmd.class);

    @Override
    public String execute(HttpServletRequest request) throws IOException, ServletException {
        log.debug("UserRemoveCmd starts");

        String userId = request.getParameter(ContextParam.USER_ID_TO_PROCESS);

        if (UserRepositoryImpl.removeUser(Integer.parseInt(userId))) {
            log.debug("UserRemoveCmd finished");
            return Command.LIST_USERS_LIBRARIANS.getPath();
        }

        log.debug("UserRemoveCmd finished with error");
        return getErrorPageWithMessage(request,
                "Error of removing librarian, your request was not completed, try again, please.");

    }
}
