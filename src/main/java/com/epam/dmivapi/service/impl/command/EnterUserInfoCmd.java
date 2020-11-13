package com.epam.dmivapi.service.impl.command;

import com.epam.dmivapi.Path;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class EnterUserInfoCmd extends AbstractCmd {
    private static final Logger log = Logger.getLogger(EnterUserInfoCmd.class);

    @Override
    public String execute(HttpServletRequest request) throws IOException, ServletException {
        log.debug("EnterUserInfoCmd starts");

        log.debug("EnterUserInfoCmd finished");
        return Path.PAGE__ENTER_USER_INFO;
    }
}
