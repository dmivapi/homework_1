package com.epam.dmivapi.web.command;

import com.epam.dmivapi.web.ContextParam;
import com.epam.dmivapi.web.Path;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class NoCmd extends AbstractCmd {
    private static final Logger log = Logger.getLogger(NoCmd.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        log.debug("NoCmd starts");

        log.debug("NoCmd finished");
        return ContextParam.getCurrentUserRole(request.getSession()).getDefaultPage();
    }
}
