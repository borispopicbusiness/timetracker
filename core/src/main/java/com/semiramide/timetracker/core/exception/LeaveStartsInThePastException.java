package com.semiramide.timetracker.core.exception;

public class LeaveStartsInThePastException extends TimetrackerException {

  public LeaveStartsInThePastException(String message) {
    super(ErrorCode.LEAVE_START_IN_PAST, message);
  }
}
