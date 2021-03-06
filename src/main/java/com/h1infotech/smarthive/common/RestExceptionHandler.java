package com.h1infotech.smarthive.common;

import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Created by Johnson on 2018/05/11.
 */
@ControllerAdvice
public class RestExceptionHandler {
    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Response<String> handleRuntimeException(HttpServletRequest req, Exception ex) {
        String errorMessage = localizeErrorMessage(ex.getMessage());
        return Response.fail(BizCodeEnum.SERVICE_ERROR.getCode(), errorMessage);
    }

    private String localizeErrorMessage(String errorCode) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(errorCode, null, locale);
    }

}