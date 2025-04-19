package com.semiramide.timetracker.core.exception;

public class FreeDaysLeftException extends TimetrackerException {
    public FreeDaysLeftException(String m) {
        super(ErrorCode.LESS_FREE_DAYS_LEFT_THAN_REQUESTED, m);
    }
}
