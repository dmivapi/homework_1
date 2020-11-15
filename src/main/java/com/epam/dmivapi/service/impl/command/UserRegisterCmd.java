package com.epam.dmivapi.service.impl.command;

import com.epam.dmivapi.repository.impl.UserRepositoryImpl;
import com.epam.dmivapi.model.User;
import com.epam.dmivapi.dto.Role;
import com.epam.dmivapi.ContextParam;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static com.epam.dmivapi.ContextParam.*;

public class UserRegisterCmd extends AbstractCmd {
    private static final Logger log = Logger.getLogger(UserRegisterCmd.class);

    @Override
    public String execute(HttpServletRequest request) throws IOException, ServletException {
        log.debug("UserRegisterCmd starts");

        HttpSession session = request.getSession();
        Role role = getCurrentUserRole(session);
        User userFromForm = getUserInfoFromForm(request);

        // error handler
        if (userFromForm == null)
            return getErrorPageWithMessage(request,"User's fields cannot be empty");

        // check if this user already exists
        User userFromDB = UserRepositoryImpl.findUserByLogin(userFromForm.getEmail());
        log.trace("Found in DB: user --> " + userFromDB);

        if (userFromDB != null)
            return getErrorPageWithMessage(request,"This user is already registered, please login instead");

        User loggedInUser = ContextParam.getCurrentUser(session);

        if (loggedInUser == null) { // self registration
            userFromForm.setUserRole(Role.READER);
            if (!UserRepositoryImpl.updateUser(userFromForm, true)) // true - for creating a new one
                return getErrorPageWithMessage(request,"Something went wrong: registration of user failed");

            ContextParam.setCurrentUser(session, userFromForm);
            ContextParam.setCurrentUserRole(session, userFromForm.getUserRole());
            log.info("User " + userFromForm + "has registered and now is logged as " +
                    userFromForm.getUserRole().toString().toLowerCase()
            );

            log.debug("UserRegisterCmd finished");
            return userFromForm.getUserRole().getDefaultPage();
        }

//        if (loggedInUser.getUserRole() == Role.ADMIN) { // registering a new librarian
            userFromForm.setUserRole(Role.LIBRARIAN);
            if (!UserRepositoryImpl.updateUser(userFromForm, true)) // true - for creating a new one
                return getErrorPageWithMessage(request,"Something went wrong: registration of user failed");

            log.debug("UserRegisterCmd finished");
            return Command.LIST_USERS_LIBRARIANS.getPath();
//        }
    }

    private boolean passwordCheck(User user, String password) {
        return password.equals(user.getPassword());
    }

    /**
     * Gets part of user information from an edit form
     * some fields are left in their default values and
     * will be either set manually later (like user role)
     * or kept to their default values (isBlocked - false)
     * TODO decide what to do with locale
     * @param request
     * @return
     */
    private static User getUserInfoFromForm(HttpServletRequest request) {
        User user = new User();
        boolean userValid = false;

        // getting information filled in registration form
        String login = request.getParameter(ContextParam.USR_LOGIN);
        log.trace("Request parameter: loging --> " + login);
        if (login != null && !login.isEmpty()) {
            user.setEmail(login);
            userValid = true;
        }

        String password = request.getParameter(ContextParam.USR_PASSWORD);
        if (password != null && !password.isEmpty()) {
            user.setPassword(password);
            userValid = true;
        }

        String firstName = request.getParameter(ContextParam.USR_FIRST_NAME);
        if (firstName != null && !firstName.isEmpty()) {
            user.setFirstName(firstName);
            userValid = true;
        }

        String lastName = request.getParameter(ContextParam.USR_LAST_NAME);
        if (firstName != null && !firstName.isEmpty()) {
            user.setLastName(firstName);
            userValid = true;
        }

        return userValid ? user : null;
    }
}
