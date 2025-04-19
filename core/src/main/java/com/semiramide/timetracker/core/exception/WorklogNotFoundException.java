package com.semiramide.timetracker.core.exception;

public class WorklogNotFoundException extends TimetrackerException {
    public WorklogNotFoundException(String m) {
        super(ErrorCode.WORKLOG_NOT_FOUND, m);
    }
}
