package com.semiramide.timetracker.core.exception;

public class AlreadyASubordinateException extends TimetrackerException {

  public AlreadyASubordinateException(String m) {
    super(ErrorCode.ALREADY_A_SUBORDINATE, m);
  }
}
