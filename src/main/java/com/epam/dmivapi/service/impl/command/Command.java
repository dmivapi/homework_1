package com.epam.dmivapi.service.impl.command;

import com.epam.dmivapi.ContextParam;
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

    LIST_BOOKS("/book/list", null, false),
    BOOK_NEW("/book/new", null, true),
    BOOK_REMOVE("/book/remove", null, true),

    LIST_LOANS_OF_SELF("/loan/listSelf", null, false),
    LIST_LOANS_OF_USER("/loan/", null, false),
    LIST_LOANS_OF_ALL("/loan", null, false),

    LIST_USERS_LIBRARIANS("/user/admin/librarians", null, false),
    LIST_USERS_READERS_FOR_ADMIN("/user/admin/borrowers",null, false),
    LIST_USERS_READERS_FOR_LIBRARIAN("/user/librarian/borrowers",null, false),

    LOAN_NEW("/new", null, false),
    LOAN_OUT("/out", null, false),
    LOAN_REMOVE("/delete", null, false),
    LOAN_IN("/in", null, false);

    private static final Logger log = Logger.getLogger(Command.class);

    public final String systemName;
    private final AbstractCmd implementation;
    private final boolean stateChanger; // if it does (1) we use redirect after it's execution,
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
