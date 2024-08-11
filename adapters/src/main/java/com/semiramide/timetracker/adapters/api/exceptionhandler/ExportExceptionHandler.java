package com.semiramide.timetracker.adapters.api.exceptionhandler;

import com.semiramide.timetracker.adapters.api.response.ErrorResponse;
import com.semiramide.timetracker.core.exception.MissingParameterException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ExportExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingParameterException.class)
    public ErrorResponse handleEmailAlreadyExistsException(MissingParameterException exception) {
        return ErrorResponse.getInstance(exception);
    }

}
