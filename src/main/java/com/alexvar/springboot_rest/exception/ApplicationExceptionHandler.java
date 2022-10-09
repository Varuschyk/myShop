package com.alexvar.springboot_rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ModelAndView handleUserNotFoundException(Exception exception,
                                                    HttpServletRequest request, HttpServletResponse response){
        ModelAndView modelAndView = new ModelAndView("404", HttpStatus.NOT_FOUND);
        modelAndView.addObject("info", exception.getMessage());

        return modelAndView;
    }

    @ExceptionHandler(NullEntityException.class)
    public ModelAndView handleNullEntityException(Exception exception,
                                                  HttpServletRequest request, HttpServletResponse response){
        ModelAndView modelAndView = new ModelAndView("400", HttpStatus.BAD_REQUEST);
        modelAndView.addObject("info", exception.getMessage());

        return modelAndView;
    }

    @ExceptionHandler(UserExistsException.class)
    public ModelAndView handleUserExistsException(Exception exception,
                                                  HttpServletRequest request, HttpServletResponse response){
        ModelAndView modelAndView = new ModelAndView("400", HttpStatus.BAD_REQUEST);
        modelAndView.addObject("info", exception.getMessage());

        return modelAndView;
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ModelAndView handleForbiddenException(Exception exception,
                                                  HttpServletRequest request, HttpServletResponse response){
        ModelAndView modelAndView = new ModelAndView("403", HttpStatus.FORBIDDEN);
        modelAndView.addObject("info", exception.getMessage());

        return modelAndView;
    }
}
