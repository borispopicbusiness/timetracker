package com.semiramide.timetracker.core.exception;

public class WorklogDateInFutureException extends TimetrackerException {

  public WorklogDateInFutureException(String message) {
    super(ErrorCode.WORKLOG_DATE_IN_FUTURE, message);
  }
}
