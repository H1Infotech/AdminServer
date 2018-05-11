package com.h1infotech.smarthive.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * Created by Johnson on 2018/05/11.
 */
@ControllerAdvice
public class RestExceptionHandler {
    @Autowired
    private MessageSource messageSource;

    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public Response handleRuntimeException(HttpServletRequest req, RuntimeException ex) {
        String errorMessage = localizeErrorMessage(ex.getMessage());
        return Response.fail(errorMessage);
    }

    private String localizeErrorMessage(String errorCode) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(errorCode, null, locale);
    }

}