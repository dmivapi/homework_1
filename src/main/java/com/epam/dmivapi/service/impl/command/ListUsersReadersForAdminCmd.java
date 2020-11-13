package com.epam.dmivapi.service.impl.command;

import com.epam.dmivapi.repository.impl.UserRepositoryImpl;
import com.epam.dmivapi.entity.User;
import com.epam.dmivapi.entity.User.Role;
import com.epam.dmivapi.ContextParam;
import com.epam.dmivapi.Path;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


public class ListUsersReadersForAdminCmd extends ListWithPaginationCmd {
    private static final Logger log = Logger.getLogger(ListUsersReadersForAdminCmd.class);

    @Override
    protected boolean getListToSession(HttpServletRequest request, int currentPage, int recordsPerPage) {
        List<User> users = UserRepositoryImpl.findUsers(Role.READER, null,
                currentPage, recordsPerPage);

        request.setAttribute(ContextParam.USR_USERS, users);
        log.trace("Set the request attribute: users --> " + users);
        return users != null;
    }

    @Override
    protected int getNumberOfRows(HttpServletRequest request) {
        return UserRepositoryImpl.countUsers(Role.READER, null);
    }

    @Override
    protected String getForwardPage() {
        return Path.PAGE__LIST_USERS_READERS_FOR_ADMIN;
    }
}
