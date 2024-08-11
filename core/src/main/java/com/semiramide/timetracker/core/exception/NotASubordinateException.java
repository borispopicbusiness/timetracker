package com.semiramide.timetracker.core.exception;

public class NotASubordinateException extends TimetrackerException {

  public NotASubordinateException(String m) {
    super(ErrorCode.NOT_A_SUBORDINATE, m);
  }
}
