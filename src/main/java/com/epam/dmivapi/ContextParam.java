package com.epam.dmivapi;

import com.epam.dmivapi.dto.Role;
import com.epam.dmivapi.model.User;
import org.apache.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.jstl.core.Config;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ContextParam {
    private static final Logger log = Logger.getLogger(ContextParam.class);

    // strings used in get/post request
    public static final String CONTROLLER_SERVLET = "/controller";
    public static final String COMMAND = "command";
    public static final String SELF_COMMAND = "selfCommand";
    public static final String SELF_PAGE = "selfPage";

    // id for the html form used for submitting data
    // at all app pages
    public static final String MAIN_PAGE_FORM = "mainPageForm";

    // cmdOption possible values:
    public static final String BLOCK_OPTION = "blkBlock";

    public static final String ERROR_MESSAGE = "errorMessage";

    // for pagination purposes
    public static final String PGN_NUMBER_OF_PAGES = "pgnNumberOfPages";
    public static final String PGN_CURRENT_PAGE = "pgnCurrentPage";
    public static final String PGN_RECORDS_PER_PAGE = "pgnRecordsPerPage";
    public static final String RECORDS_PER_PAGE = "3"; // TODO change this hardcoding to user entered value

    public static final String LOG4J_CONFIG_LOCATION = "log4j-config-location";

    // library system defaults parameters names
    public static final String DEFAULT_LOAN_TERM_IN_DAYS = "default-loan-term-in-days";

    // session settings
    public static final String LOCALES = "locales";
    public static final String CURRENT_USER_ROLE = "currentUserRole";
    public static final String CURRENT_USER = "currentUser";
    public static final String CURRENTLY_PROCESSED_USER = "currentlyProcessedUser";

    public static final String DEFAULT_LOCALE = "defaultLocale"; // app level
    public static final String CURRENT_LOCALE = "currentLocale"; // session level

    // book related fields
    public static final String BK_TITLE = "bsTitle";
    public static final String BK_AUTHOR = "bsAuthor";
    public static final String BK_AUTHORS = "bsAuthors";
    public static final String BK_PUBLISHER = "bsPublisher";
    public static final String BK_PUBLISHERS = "bsPublishers";
    public static final String BK_YEAR = "bsYear";
    public static final String BK_PRICE = "bsPrice";
    public static final String BK_GENRE = "bsGenre";
    public static final String BK_GENRES = "bsGenres";
    public static final String BK_LIB_CODE_BASE = "bsLibCodeBase";
    public static final String BK_QUANTITY = "bsQuantity";


    // book search parameters
    public static final String BS_ORDER_BY = "bsOrderBy";
    public static final String BS_ORDER_BY_DIRECTION = "bsOrderByDirection";
    public static final String BS_BOOKS = "bsBooks";

    // a common key for storing selected userIds as session attribute to use in DAO
    public static final String USER_ID_TO_PROCESS = "userIdToProcess";
    // a common key for storing selected user's block statuses as session attribute to use in DAO
    public static final String USER_BLOCK_TO_PROCESS = "userBlocksToProcess";

    // this key is used for storing as session attribute publications ids
    // for future processing in DAO
    public static final String PUBLICATIONS_IDS_TO_PROCESS = "lnPublicationId";

    // a common key used for storing as session attribute book copies ids
    // for future processing in DAO
    public static final String BOOK_COPIES_IDS_TO_PROCESS = "bookCopiesIdsToProcess";

    // parameter name for passing loans list to view (as session attribute)
    public static final String LS_LOANS = "lsLoans";
    // selected loan ids
    public static final String LOAN_ID_TO_PROCESS = "loanIdToProcess";

    // users list attribute key
    public static final String USR_USERS = "usrUsers";

    // login parameters
    public static final String USR_LOGIN = "usrLogin";
    public static final String USR_PASSWORD = "usrPassword";
    public static final String USR_FIRST_NAME = "usrFirstName";
    public static final String USR_LAST_NAME = "usrLastName";

    // logged user information attribute key
    public static final String LN_USER = "user";

    public static List<String> getLocalesList(HttpSession session) {
        return (List<String>) session.getServletContext().getAttribute(ContextParam.LOCALES);
    }

    public static void setLocalesList(ServletContext servletContext, String localesString) {
        List<String> locales;
        if (localesString == null || localesString.isEmpty()) {
            locales = new ArrayList<>();
            log.warn("'" + ContextParam.LOCALES + "' " + "init parameter is empty," +
                    " the default encoding will be used");
        } else
            locales = Arrays.asList(localesString.split("\\s"));
        log.debug("Application attribute set: locales --> " + locales);

        servletContext.setAttribute(ContextParam.LOCALES, locales);
    }

    public static String getDefaultLocale(ServletContext servletContext) {
        return (String) servletContext.getAttribute(ContextParam.DEFAULT_LOCALE);
    }

    public static void setDefaultLocale(ServletContext servletContext, String defaultLocale) {
        if(defaultLocale ==null)
            defaultLocale ="en";

        servletContext.setAttribute(ContextParam.DEFAULT_LOCALE,defaultLocale);
    }

    public static String getCurrentLocale(HttpSession session) {
        return (String) session.getAttribute(ContextParam.CURRENT_LOCALE);
    }

    public static void setCurrentLocale(HttpSession session, String newLocale) {
        // if newLocale is not valid - keep current locale
        if (newLocale == null || !getLocalesList(session).contains(newLocale)) {
            log.warn("Session attribute was not set: CURRENT_LOCALE --> " + newLocale);
            return;
        }
        session.setAttribute(ContextParam.CURRENT_LOCALE, newLocale);
        Config.set(session, Config.FMT_LOCALE, newLocale); // for jsp pages i18n
        log.trace("Set the session attribute: CURRENT_LOCALE --> " + newLocale);
    }

    public static User getCurrentUser(HttpSession session) {
        User user = null;
        user = (User) session.getAttribute(ContextParam.CURRENT_USER);
        return user;
    }

    public static void setCurrentUser(HttpSession session, User user) {
        if (user == null)
            session.removeAttribute(ContextParam.CURRENT_USER);
        session.setAttribute(ContextParam.CURRENT_USER, user);
        log.trace("Set the session attribute: user --> " + user);
    }

    public static Role getCurrentUserRole(HttpSession session) {
        Role role = null;
        role = (Role) session.getAttribute(ContextParam.CURRENT_USER_ROLE);
        //return role == null ? Role.GUEST : role;
        return Role.LIBRARIAN;
    }

    public static void setCurrentUserRole(HttpSession session, Role role) {
        session.setAttribute(ContextParam.CURRENT_USER_ROLE, role);
        log.trace("Set the session attribute: userRole --> " + role);
    }

    public static String getErrorPageWithMessage(HttpServletRequest request, String errorMessage) {
        request.getSession().setAttribute(ContextParam.ERROR_MESSAGE, errorMessage);
        log.error("errorMessage --> " + errorMessage);
        return Path.PAGE__ERROR;
    }

    // set of getters and setters for pagination parameters
    public static String getPgnNumberOfPages(HttpServletRequest request) {
        return (String) request.getAttribute(PGN_NUMBER_OF_PAGES);
    }

    public static void setPgnNumberOfPages(HttpServletRequest request, Integer numberOfPages) {
        request.setAttribute(PGN_NUMBER_OF_PAGES, numberOfPages);
    }

/*    public static int getPgnCurrentPage(HttpServletRequest request) {
        String curPage = (String) request.getParameter(ContextParam.PGN_CURRENT_PAGE);
        int cPage = (curPage == null || curPage.isEmpty()) ?
                1 : Integer.parseInt(curPage);
        log.debug("getPgnCurrentPage: " + curPage + " ---> " + cPage);
        return cPage;
    }

    public static Integer getPgnRecordsPerPage(HttpServletRequest request) {
        Integer rpp = (Integer) request.getAttribute(ContextParam.PGN_RECORDS_PER_PAGE);
        return (rpp == null) ?
                // default value is initialised in SessionListener
                (Integer) request.getSession().getAttribute(ContextParam.PGN_RECORDS_PER_PAGE) :
                rpp;
    }

    public static void setPgnRecordsPerPage(HttpServletRequest request, Integer numberOfPages) {
        request.setAttribute(PGN_RECORDS_PER_PAGE, numberOfPages);
    }*/

    private ContextParam() {}
}
