package com.epam.dmivapi.service.impl.command;

import com.epam.dmivapi.ContextParam;
import com.epam.dmivapi.Path;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static com.epam.dmivapi.ContextParam.setCurrentLocale;

public class SwitchLocaleCmd extends AbstractCmd {
    private static final Logger log = Logger.getLogger(SwitchLocaleCmd.class);

    @Override
    public String execute(HttpServletRequest request) throws IOException, ServletException {
        log.debug("SwitchLocaleCmd starts");
        setCurrentLocale(request.getSession(), request.getParameter(ContextParam.CURRENT_LOCALE));
        log.debug("SwitchLocaleCmd finished");
        return Path.getCurrentPageName(request);
    }
}
