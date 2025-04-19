package com.semiramide.timetracker.core.exception;

public class LeaveStartAfterEndException extends TimetrackerException {
    public LeaveStartAfterEndException(String message) {
        super(ErrorCode.LEAVE_START_AFTER_END, message);
    }
}
