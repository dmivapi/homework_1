package com.epam.dmivapi.web.command;

import com.epam.dmivapi.db.dao.UserDao;
import com.epam.dmivapi.db.entity.User;
import com.epam.dmivapi.db.entity.User.Role;
import com.epam.dmivapi.web.ContextParam;
import com.epam.dmivapi.web.Path;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


public class ListUsersReadersForLibrarianCmd extends ListWithPaginationCmd {
    private static final Logger log = Logger.getLogger(ListUsersReadersForLibrarianCmd.class);

    @Override
    protected boolean getListToSession(HttpServletRequest request, int currentPage, int recordsPerPage) {
        List<User> users = UserDao.findUsers(Role.READER, null,
                currentPage, recordsPerPage);

        request.setAttribute(ContextParam.USR_USERS, users);
        log.trace("Set the request attribute: users --> " + users);
        return users != null;
    }

    @Override
    protected int getNumberOfRows(HttpServletRequest request) {
        return UserDao.countUsers(Role.READER, null);
    }

    @Override
    protected String getForwardPage() {
        return Path.PAGE__LIST_USERS_READERS_FOR_LIBRARIAN;
    }
}
