package com.epam.dmivapi.web;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


import com.epam.dmivapi.web.command.AbstractCmd;
import com.epam.dmivapi.web.command.Command;
import org.apache.log4j.Logger;

public class LibController extends HttpServlet {
    private static final Logger logger = Logger.getLogger(LibController.class);
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request, response);
    }

    private void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        logger.debug("LibController starts");

        String[] commandParams = request.getParameterValues(ContextParam.COMMAND);
        String commandName = null;
        if (commandParams != null) {
            for (String cName : commandParams) {
                if (!cName.isEmpty()) {
                    commandName = cName;
                    break;
                }
            }
        }
        logger.trace("Request parameter: command = " + commandName);

        Command command = Command.safeValueOf(commandName);
        logger.trace("Obtained command --> " + command);

        String forward = command.getImplementation().execute(request, response);
        logger.trace("Next page: " + forward);

        logger.debug("LibController finished, now go to forward address --> " + forward);

        if (command.isStateChanger()) {
            response.sendRedirect(forward);
        } else {
            RequestDispatcher disp = request.getRequestDispatcher(forward);
            disp.forward(request, response);
        }
    }
}
