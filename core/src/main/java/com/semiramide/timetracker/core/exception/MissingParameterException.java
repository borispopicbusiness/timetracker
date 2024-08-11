package com.semiramide.timetracker.core.exception;

public class MissingParameterException extends TimetrackerException {

    public MissingParameterException(String message) {
        super(ErrorCode.MISSING_PARAMETER, message);
    }

}
