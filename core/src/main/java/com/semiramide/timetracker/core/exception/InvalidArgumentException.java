package com.semiramide.timetracker.core.exception;

public class InvalidArgumentException extends TimetrackerException {
    public InvalidArgumentException(String m) {
        super(ErrorCode.INVALID_ARGUMENT, m);
    }
}
