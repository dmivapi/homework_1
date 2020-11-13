package com.epam.dmivapi.config;

import com.epam.dmivapi.ContextParam;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.jsp.jstl.core.Config;

public class LibraryApplicationInitializer implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext webApplicationContext = new AnnotationConfigWebApplicationContext();
        webApplicationContext.register(LibraryAppConfig.class);

        DispatcherServlet dispatcherServlet = new DispatcherServlet(webApplicationContext);
        ServletRegistration.Dynamic libraryDispatcherServlet =
                servletContext.addServlet("controller", dispatcherServlet);

        servletContext.setInitParameter(ContextParam.DEFAULT_LOAN_TERM_IN_DAYS,"30");
        servletContext.setInitParameter(ContextParam.LOG4J_CONFIG_LOCATION,"/WEB-INF/log4j.properties");
        servletContext.setInitParameter(Config.FMT_LOCALIZATION_CONTEXT, "resources");
        servletContext.setInitParameter(Config.FMT_LOCALE, "en");
        servletContext.setInitParameter(ContextParam.LOCALES, "ru en");

        libraryDispatcherServlet.setLoadOnStartup(1);
        libraryDispatcherServlet.addMapping("/");
    }
}
