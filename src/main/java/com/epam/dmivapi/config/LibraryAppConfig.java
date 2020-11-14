package com.epam.dmivapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.servlet.*;
import java.io.IOException;

@EnableWebMvc
@Configuration
@ComponentScan(basePackages="com.epam.dmivapi")
public class LibraryAppConfig {

    @Bean
    protected InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/views/");
        viewResolver.setSuffix(".jsp");

        return  viewResolver;
    }

    @Bean
    public Filter characterEncodingFilter() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        return new FilterWrapper(characterEncodingFilter);
    }

    class FilterWrapper implements Filter {
        private Filter filter;
        public FilterWrapper(Filter filter) {
            this.filter = filter;
        }

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            System.out.println("!!!!!!!!! DOING CHAR FILTER !!!!!!!!!!!!");
            filter.doFilter(request, response, chain);
        }
    }
}
