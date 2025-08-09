package com.orionhiro.ArticlesApp.utils;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFoundException(Exception exception){
        log.warn(exception.getMessage());
        return "notFound";
    }

    @ExceptionHandler(InternalServerError.class)
    public String handleServerErrorException(Exception exception){
        log.warn(exception.toString());
        return "internalServerError";
    }
}
