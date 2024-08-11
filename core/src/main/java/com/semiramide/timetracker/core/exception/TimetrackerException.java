package com.semiramide.timetracker.core.exception;

import lombok.Getter;

@Getter
public class TimetrackerException extends RuntimeException {

  private final String code;
  private final String message;

  public TimetrackerException(String c, String m) {
    super(m);
    this.code = c;
    this.message = m;
  }
}
