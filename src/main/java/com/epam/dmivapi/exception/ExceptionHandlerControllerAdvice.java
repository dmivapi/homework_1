package com.epam.dmivapi.exception;

import com.epam.dmivapi.Path;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionHandlerControllerAdvice {
    private static final Logger LOGGER = Logger.getLogger(ExceptionHandlerControllerAdvice.class);

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public String handleEntityAlreadyExistsException(EntityAlreadyExistsException e, Model model) {
        LOGGER.error(e.getMessage());
        return Path.PAGE__ERROR;
    }

    @ExceptionHandler(EntityDoesNotExistException.class)
    public String handleEntityDoesNotExistException(EntityDoesNotExistException e, Model model) {
        LOGGER.error(e.getMessage());
        return Path.PAGE__ERROR;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(RuntimeException.class)
    public String handleRuntimeException(RuntimeException e, Model model) {
        LOGGER.error(e.getMessage());
        model.addAttribute("errorMessage", "The source of this error is currently unknown");
        return Path.PAGE__ERROR;
    }
}
