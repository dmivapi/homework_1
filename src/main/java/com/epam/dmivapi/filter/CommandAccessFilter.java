package com.epam.dmivapi.filter;

import com.epam.dmivapi.dto.Role;
import com.epam.dmivapi.ContextParam;
import com.epam.dmivapi.Path;
import com.epam.dmivapi.service.impl.command.Command;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class CommandAccessFilter implements Filter {
    private static final Logger log = Logger.getLogger(CommandAccessFilter.class);

    // parameters names for web.xml
    private static final String PARAM_COMMON_NAME = "common-access";
    private static final String PARAM_GUEST_NAME = "guest-access";

    // commands access
    private static Map<Role, Set<Command>> roleAccessRights = new EnumMap<>(Role.class);
    private static Set<Command> commonAccessRights = EnumSet.noneOf(Command.class);
    private static Set<Command> guestAccessRights = EnumSet.noneOf(Command.class);;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.debug("CommandAccessFilter starts");

        if (accessAllowed(servletRequest)) {
            log.debug("CommandAccessFilter starts");
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            String errorMessage = "You do not have a permission to access the requested resource";

            servletRequest.setAttribute(ContextParam.ERROR_MESSAGE, errorMessage); // TODO might not be displayed from request
            log.trace("Set the request attribute: errorMessage --> " + errorMessage);

            servletRequest.getRequestDispatcher(Path.PAGE__ERROR) // TODO double check this
                    .forward(servletRequest, servletResponse);
        }
    }

    private boolean accessAllowed(ServletRequest request) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        String commandName = request.getParameter(ContextParam.COMMAND);
        if (commandName == null || commandName.isEmpty())
            return false;

        Command cmd = Command.safeValueOf(commandName);
        if (guestAccessRights.contains(cmd))
            return true;

        HttpSession session = httpRequest.getSession(false);
        if (session == null)
            return false;

        Role userRole = (Role) session.getAttribute(ContextParam.CURRENT_USER_ROLE);
        if (userRole == null)
            return false;

        return roleAccessRights.get(userRole).contains(cmd)
                || commonAccessRights.contains(cmd);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.debug("CommandAccessFilter initialization starts");

        for (Role role : Role.values())
            roleAccessRights.put(role,
                    listAccessRightsFromConfig(role.name().toLowerCase(), filterConfig)
            );
        log.trace("AccessRights map --> " + roleAccessRights);

        commonAccessRights = listAccessRightsFromConfig(PARAM_COMMON_NAME, filterConfig);
        log.trace("Common commands available for authorized users --> " + commonAccessRights);

        guestAccessRights = listAccessRightsFromConfig(PARAM_GUEST_NAME, filterConfig);
        log.trace("Commands available for unauthorized guests --> " + guestAccessRights);

        log.debug("CommandAccessFilter initialization finished");
    }

    @Override
    public void destroy() {
        log.debug("CommandAccessFilter destruction starts");
        // do nothing
        log.debug("CommandAccessFilter destruction finished");
    }

    private Set<Command> listAccessRightsFromConfig(String paramName, FilterConfig filterConfig) {
        String strParameter = filterConfig.getInitParameter(paramName);

        if (strParameter == null) {
            log.warn("Parameter: " + paramName + "is missing in configuration");
            return EnumSet.noneOf(Command.class);
        }

        return Arrays.stream(strParameter.split("\\s"))
                .map(Command::safeValueOf)
                .collect(Collectors.toCollection(()->EnumSet.noneOf(Command.class)));
    }
}
