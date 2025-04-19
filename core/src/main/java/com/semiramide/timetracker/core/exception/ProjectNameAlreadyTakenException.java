package com.semiramide.timetracker.core.exception;

public class ProjectNameAlreadyTakenException extends TimetrackerException {
    public ProjectNameAlreadyTakenException(String m) {
        super(ErrorCode.NAME_ALREADY_TAKEN, m);
    }
}
