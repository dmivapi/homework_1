package com.epam.dmivapi.web.command;

import com.epam.dmivapi.db.entity.User.Role;
import com.epam.dmivapi.db.dao.UserDao;
import com.epam.dmivapi.db.entity.User;
import com.epam.dmivapi.web.ContextParam;
import com.epam.dmivapi.web.Path;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static com.epam.dmivapi.web.ContextParam.*;

public class UserLoginCmd extends AbstractCmd {
    private static final Logger log = Logger.getLogger(UserLoginCmd.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        log.debug("LoginCommand starts");

        HttpSession session = request.getSession();

        String login = request.getParameter(ContextParam.USR_LOGIN);
        log.trace("Request parameter: loging --> " + login);
        String password = request.getParameter(ContextParam.USR_PASSWORD);

        if (login == null || password == null || login.isEmpty() || password.isEmpty())
            return getErrorPageWithMessage(request, "Login/password cannot be empty");

        User user = new UserDao().findUserByLogin(login);
        log.trace("Found in DB: user --> " + user);

        if (user == null || !passwordCheck(user, password))
            return getErrorPageWithMessage(request, "Cannot find user with such login/password");

        Role userRole = user.getUserRole();
        ContextParam.setCurrentUserRole(session, userRole);
        ContextParam.setCurrentUser(session, user);
        log.info("User " + user + " logged as " + userRole.toString().toLowerCase());

        setCurrentLocale(session, user.getLocaleName());

        log.debug("LoginCommand finished");

        return userRole.getDefaultPage();
    }

    private boolean passwordCheck(User user, String password) {
        return password.equals(user.getPassword());
    }
}
