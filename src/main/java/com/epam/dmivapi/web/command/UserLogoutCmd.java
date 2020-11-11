package com.epam.dmivapi.web.command;

import com.epam.dmivapi.db.entity.User;
import com.epam.dmivapi.web.ContextParam;
import com.epam.dmivapi.web.Path;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class UserLogoutCmd extends AbstractCmd {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession();

        ContextParam.setCurrentUserRole(session, User.Role.GUEST);
        ContextParam.setCurrentUser(session, null);

        return Path.PAGE__LOGOUT;
    }
}
