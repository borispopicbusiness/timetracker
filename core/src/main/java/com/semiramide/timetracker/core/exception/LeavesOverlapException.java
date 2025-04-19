package com.semiramide.timetracker.core.exception;

public class LeavesOverlapException extends TimetrackerException {
    public LeavesOverlapException(String message) {
        super(ErrorCode.LEAVES_OVERLAP, message);
    }
}
