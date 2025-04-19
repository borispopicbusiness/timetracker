package com.semiramide.timetracker.core.exception;

public class WorklogDateLockedException extends TimetrackerException {
    public WorklogDateLockedException(String message) {
        super(ErrorCode.WORKLOG_DATE_LOCKED, message);
    }
}
