package com.epam.dmivapi.web.command;

import com.epam.dmivapi.web.ContextParam;
import org.apache.log4j.Logger;


public enum Command {
    NO_COMMAND("", new NoCmd(), false),
    SWITCH_LOCALE("switchLocale", new SwitchLocaleCmd(), false),

    ENTER_USER_INFO("enterUserInfo", new EnterUserInfoCmd(), false),
    ENTER_BOOK_INFO("enterBookInfo", new EnterBookInfoCmd(), false),

    LOGIN("login", new UserLoginCmd(), true),
    REGISTER("register", new UserRegisterCmd(), true),
    LOGOUT("logout", new UserLogoutCmd(), true),
    USER_NEW("userNew", null, true),
    USER_BLOCK("userBlock", new UserBlockCmd(), false),
    USER_REMOVE("userRemove", new UserRemoveCmd(), true),

    LIST_BOOKS("listBooks", new ListBooksCmd(), false),
    BOOK_NEW("bookNew", new BookNewCmd(), true),
    BOOK_REMOVE("bookRemove", new BookRemoveCmd(), true),

    LIST_LOANS_OF_SELF("listLoansOfSelf", new ListLoansOfSelfCmd(), false),
    LIST_LOANS_OF_USER("listLoansOfUser", new ListLoansOfUserCmd(), false),
    LIST_LOANS_OF_ALL("listLoansOfAll", new ListLoansOfAllCmd(), false),

    LIST_USERS_LIBRARIANS("listUsersLibrarians", new ListUsersLibrariansCmd(), false),
    LIST_USERS_READERS_FOR_ADMIN("listUsersReadersForAdmin",new ListUsersReadersForAdminCmd(), false),
    LIST_USERS_READERS_FOR_LIBRARIAN("listUsersReadersForLibrarian",new ListUsersReadersForLibrarianCmd(), false),

    LOAN_NEW("loanNew", new LoanNewCmd(), false),
    LOAN_OUT("loanOut", new LoanOutCmd(), false),
    LOAN_REMOVE("loanRemove", new LoanRemoveCmd(), false),
    LOAN_IN("loanIn", new LoanInCmd(), false);

    private static final Logger log = Logger.getLogger(Command.class);

    private String systemName;
    private AbstractCmd implementation;
    private boolean stateChanger; // if it does (1) we use redirect after it's execution,
                                  //            (2) we do not save it as a command that is possible to return to


    Command(String systemName, AbstractCmd implementation, boolean stateChanger) {
        this.implementation = implementation;
        this.systemName = systemName;
        this.stateChanger = stateChanger;
    }

    public String getSystemName() {
        return systemName;
    }
    public AbstractCmd getImplementation() { return implementation; }
    public boolean isStateChanger() { return stateChanger; }

    public static Command safeValueOf(String commandName) {
        if (commandName != null) {
            String simplifiedCmdName = commandName.replaceAll("_", "");
            for (Command cmd : values()) {
                if (cmd.getSystemName().equalsIgnoreCase(simplifiedCmdName))
                    return cmd;
            }
        }
        log.trace("Command not found, name --> " + commandName);
        return NO_COMMAND;
    }

    public String getPath() {
        return ContextParam.CONTROLLER_SERVLET +
                (this == NO_COMMAND ? "" : ("?" + ContextParam.COMMAND + "=" + systemName));
    }

    @Override
    public String toString() {
        return "Command: " + systemName + " forward to: " + getPath() + " isStateChanger: " + stateChanger;
    }
}
