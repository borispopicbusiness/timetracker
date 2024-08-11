package com.semiramide.timetracker.core.entity.enums;

public enum LeaveStatus {
  OPEN("OPEN"),
  APPROVED("APPROVED"),
  DENIED("DENIED"),
  UNKNOWN("UNKNOWN");

  private String name;

  LeaveStatus(String n) {
    this.name = n;
  }

  public static LeaveStatus fromName(String n) {
    for (LeaveStatus ls : LeaveStatus.values()) {
      if (ls.getName().equals(n)) {
        return ls;
      }
    }
    return UNKNOWN;
  }

  public String getName() {
    return this.name;
  }
}
