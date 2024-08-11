package com.semiramide.timetracker.core.exception;

public class NoProjectFoundException extends TimetrackerException {
  public NoProjectFoundException(String m) {
    super(ErrorCode.NO_PROJECT_FOUND, m);
  }
}
