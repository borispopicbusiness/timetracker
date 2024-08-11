package com.semiramide.timetracker.core.exception;

public class ErrorCode {

    public static final String EMAIL_ALREADY_EXISTS = "e001";
    public static final String CYCLE_DETECTED = "e002";
    public static final String USER_NOT_FOUND = "e003";
    public static final String INVALID_ARGUMENT = "e004";
    public static final String ALREADY_A_SUBORDINATE = "e005";
    public static final String NAME_ALREADY_TAKEN = "e006";
    public static final String NO_PROJECT_FOUND = "e007";
    public static final String VALIDATION_FAILED = "e008";
    public static final String WORKLOG_NOT_FOUND = "e009";
    public static final String NODES_ARE_NOT_CONNECTED = "e010";
    public static final String NOT_A_SUBORDINATE = "e011";
    public static final String LESS_FREE_DAYS_LEFT_THAN_REQUESTED = "e012";
    public static final String LEAVES_OVERLAP = "e013";
    public static final String LEAVE_START_AFTER_END = "e014";
    public static final String LEAVE_START_IN_PAST = "e015";
    public static final String WORKLOG_DATE_LOCKED = "e016";
    public static final String WORKLOG_DATE_IN_FUTURE = "e017";
    public static final String MISSING_PARAMETER = "e018";


    public static final String GENERAL_EXCEPTION = "e999";

    private ErrorCode() {

    }

}
