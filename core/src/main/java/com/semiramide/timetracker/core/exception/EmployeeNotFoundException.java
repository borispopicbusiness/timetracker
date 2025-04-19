package com.semiramide.timetracker.core.exception;

public class EmployeeNotFoundException extends TimetrackerException {
    public EmployeeNotFoundException(String m) {
        super(ErrorCode.USER_NOT_FOUND, m);
    }
}
