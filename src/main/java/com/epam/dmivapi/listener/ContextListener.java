package com.epam.dmivapi.listener;

import com.epam.dmivapi.ContextParam;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.jsp.jstl.core.Config;
import java.io.File;

@WebListener
public class ContextListener implements ServletContextListener {
    private static final Logger log = Logger.getLogger(ContextListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log("Servlet context initialization starts");

        ServletContext servletContext = sce.getServletContext();
        initLog4J(servletContext);
        initI18N(servletContext);
        initLibrarySystem(servletContext);

        log("Servlet context initialization finished");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        log("Servlet context destruction starts");
        // do nothing
        log("Servlet context destruction finished");
    }

    /**
     * Initializes log4j framework.
     *
     * @param servletContext
     */
    private void initLog4J(ServletContext servletContext) {
        log("Log4J initialization started");

        try {
            String log4jConfigFile = servletContext.getInitParameter(ContextParam.LOG4J_CONFIG_LOCATION);
            String fullPath = servletContext.getRealPath("") + File.separator + log4jConfigFile;

            PropertyConfigurator.configure(fullPath);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        log("Log4J initialization finished");
    }

    /**
     * Initializes i18n subsystem.
     *
     * @param servletContext
     */
    private void initI18N(ServletContext servletContext) {
        log.debug("I18N subsystem initialization started");

        // list of available locales from web.xml
        ContextParam.setLocalesList(servletContext,
                servletContext.getInitParameter(ContextParam.LOCALES)
        );

        // default locale from web.xml
        ContextParam.setDefaultLocale(servletContext,
                servletContext.getInitParameter(Config.FMT_LOCALE)
        );
        log.debug("I18N subsystem initialization finished");
    }

    /**
     * Initializes Library system default settings.
     *
     * @param servletContext
     */
    private void initLibrarySystem(ServletContext servletContext) {
        log.debug("initLibrarySystem initialization started");

        String defaultLoanTerm = servletContext.getInitParameter(ContextParam.DEFAULT_LOAN_TERM_IN_DAYS);

        if (defaultLoanTerm == null || defaultLoanTerm.isEmpty() || !defaultLoanTerm.matches("\\d+")) {
            log.warn("'" + ContextParam.DEFAULT_LOAN_TERM_IN_DAYS + "' " + "init parameter is empty," +
                    " the default number of loan days will be used");
            defaultLoanTerm = "14"; // 2 weeks
        }
        Integer defLTerm = Integer.parseInt(defaultLoanTerm);

        log.debug("Application attribute set: " + ContextParam.DEFAULT_LOAN_TERM_IN_DAYS + " --> " + defLTerm);
        servletContext.setAttribute(ContextParam.DEFAULT_LOAN_TERM_IN_DAYS, defLTerm);

        log.debug("initLibrarySystem initialization finished");
    }

    private void log(String msg) {
        System.out.println("[ContextListener] " + msg);
    }
}
