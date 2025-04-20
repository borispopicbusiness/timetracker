package com.semiramide.timetracker.adapters.api.exceptionhandler;

import com.semiramide.timetracker.adapters.api.response.ErrorResponse;
import com.semiramide.timetracker.core.exception.*;
import jakarta.validation.ConstraintViolationException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class EmployeeExceptionHandler {
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ErrorResponse handleEmailAlreadyExistsException(EmailAlreadyExistsException exception) {
        return ErrorResponse.getInstance(exception);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EmployeeNotFoundException.class)
    public ErrorResponse handleEmployeeNotFoundException(EmployeeNotFoundException exception) {
        return ErrorResponse.getInstance(exception);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CycleDetectedException.class)
    public ErrorResponse handleCycleDetectedException(CycleDetectedException exception) {
        return ErrorResponse.getInstance(exception);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidArgumentException.class)
    public ErrorResponse handleInvalidArgumentException(InvalidArgumentException exception) {
        return ErrorResponse.getInstance(exception);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(AlreadyASubordinateException.class)
    public ErrorResponse handleAlreadyASubordinateException(AlreadyASubordinateException exception) {
        return ErrorResponse.getInstance(exception);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ErrorResponse handleConstraintViolationException(ConstraintViolationException exception) {
        ValidationFailedException vfe = new ValidationFailedException(exception, "");
        return ErrorResponse.getInstance(vfe);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NotReachableNodeException.class)
    public ErrorResponse handleNotReachableNodeException(NotReachableNodeException exception) {
        return ErrorResponse.getInstance(exception);
    }
}
