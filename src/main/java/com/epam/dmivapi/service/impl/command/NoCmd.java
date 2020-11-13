package com.epam.dmivapi.service.impl.command;

import com.epam.dmivapi.ContextParam;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class NoCmd extends AbstractCmd {
    private static final Logger log = Logger.getLogger(NoCmd.class);

    @Override
    public String execute(HttpServletRequest request) throws IOException, ServletException {
        log.debug("NoCmd starts");

        log.debug("NoCmd finished");
        return ContextParam.getCurrentUserRole(request.getSession()).getDefaultPage();
    }
}
