package com.epam.dmivapi.service.impl.command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public abstract class AbstractCmd {
    public abstract String execute(HttpServletRequest request)
            throws IOException, ServletException;

    @Override
    public final String toString() {
        return getClass().getSimpleName();
    }
}
