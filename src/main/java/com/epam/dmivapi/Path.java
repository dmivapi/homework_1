package com.epam.dmivapi;

import com.epam.dmivapi.service.impl.command.Command;
import org.apache.log4j.Logger;
import javax.servlet.http.HttpServletRequest;

/**
 * Provides constants with paths and some static utility methods
 * to work with them
 */

public class Path {
    private static final Logger log = Logger.getLogger(Path.class);
    // pages
    public static final String PAGE__HOME = "/jsp/home.jsp";
    public static final String PAGE__LOGOUT = "/jsp/logout.jsp";
    public static final String PAGE__ENTER_USER_INFO = "enter_user_info";
    public static final String PAGE__ENTER_BOOK_INFO = "enter_book_info";
    public static final String PAGE__ERROR = "error_page";

    public static final String PAGE__LIST_BOOKS = "listBooks";
    public static final String PAGE__LIST_LOANS_SINGLE_USER = "listLoansSingleUser";
    public static final String PAGE__LIST_LOANS_MULTIPLE_USERS = "listLoansMultipleUsers";
    public static final String PAGE__LIST_USERS_READERS_FOR_LIBRARIAN = "listUsersReadersForLibrarian";
    public static final String PAGE__LIST_USERS_READERS_FOR_ADMIN = "listUsersReadersForAdmin";
    public static final String PAGE__LIST_USERS_LIBRARIANS = "listUsersLibrarians";

    public static String getCurrentPageName(HttpServletRequest request) {
        String fullCmd = request.getParameter(ContextParam.SELF_COMMAND);
        log.debug("getCurrentPageName --> " + fullCmd);
        return (fullCmd == null) ?
            request.getParameter(ContextParam.SELF_PAGE) :
            Command.safeValueOf(fullCmd).getPath();
    }

    private Path() {}
}
