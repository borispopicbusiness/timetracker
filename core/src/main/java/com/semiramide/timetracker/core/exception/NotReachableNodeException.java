package com.semiramide.timetracker.core.exception;

public class NotReachableNodeException extends TimetrackerException {
  public NotReachableNodeException(String m) {
    super(ErrorCode.NODES_ARE_NOT_CONNECTED, m);
  }
}
