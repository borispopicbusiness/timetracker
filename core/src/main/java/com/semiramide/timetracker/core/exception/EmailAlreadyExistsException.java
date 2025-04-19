package com.semiramide.timetracker.core.exception;

public class EmailAlreadyExistsException extends TimetrackerException {
    public EmailAlreadyExistsException(String m) {
        super(ErrorCode.EMAIL_ALREADY_EXISTS, m);
    }
}
