package com.semiramide.timetracker.core.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

public class ValidationFailedException extends TimetrackerException {

  private final ConstraintViolationException exception;

  public ValidationFailedException(ConstraintViolationException e, String m) {
    super(ErrorCode.VALIDATION_FAILED, m);
    this.exception = e;
  }

  @Override
  public String getMessage() {
    List<String> messages = new ArrayList<>();
    for (ConstraintViolation<?> cv : exception.getConstraintViolations()) {
      messages.add(cv.getMessage());
    }
    return messages.toString();
  }
}
