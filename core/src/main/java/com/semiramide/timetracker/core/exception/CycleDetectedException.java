package com.semiramide.timetracker.core.exception;

public class CycleDetectedException extends TimetrackerException {
    public CycleDetectedException(String m) {
        super(ErrorCode.CYCLE_DETECTED, m);
    }
}
