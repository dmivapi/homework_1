package com.epam.dmivapi.service.impl.command;

import com.epam.dmivapi.dto.Role;
import com.epam.dmivapi.ContextParam;
import com.epam.dmivapi.Path;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class UserLogoutCmd extends AbstractCmd {
    @Override
    public String execute(HttpServletRequest request) throws IOException, ServletException {
        HttpSession session = request.getSession();

        ContextParam.setCurrentUserRole(session, Role.GUEST);
        ContextParam.setCurrentUser(session, null);

        return Path.PAGE__LOGOUT;
    }
}
