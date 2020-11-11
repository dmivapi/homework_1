package com.epam.dmivapi.web.command;

import com.epam.dmivapi.db.dao.UserDao;
import com.epam.dmivapi.db.entity.User;
import com.epam.dmivapi.web.ContextParam;
import com.epam.dmivapi.web.Path;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ListUsersLibrariansCmd extends ListWithPaginationCmd {
    private static final Logger log = Logger.getLogger(ListUsersLibrariansCmd.class);

    @Override
    protected boolean getListToSession(HttpServletRequest request, int currentPage, int recordsPerPage) {
        List<User> users = UserDao.findUsers(User.Role.LIBRARIAN, null,
                currentPage, recordsPerPage);

        request.setAttribute(ContextParam.USR_USERS, users);
        log.trace("Set the request attribute: users --> " + users);
        return users != null;
    }

    @Override
    protected int getNumberOfRows(HttpServletRequest request) {
        return UserDao.countUsers(User.Role.LIBRARIAN, null);
    }

    @Override
    protected String getForwardPage() {
        return Path.PAGE__LIST_USERS_LIBRARIANS;
    }
}


