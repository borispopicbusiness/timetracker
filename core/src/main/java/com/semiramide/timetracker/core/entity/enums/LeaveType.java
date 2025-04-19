package com.semiramide.timetracker.core.entity.enums;

public enum LeaveType {
    VACATION("VACATION"),
    SICK_LEAVE("SICK_LEAVE"),
    PARENTAL_LEAVE("PARENTAL_LEAVE"),
    UNKNOWN("UNKNOWN");

    private final String name;

    LeaveType(String n) {
        this.name = n;
    }

    public static LeaveType fromName(String n) {
        for ( LeaveType lt : LeaveType.values() ) {
            if ( lt.getName().equals(n) ) {
                return lt;
            }
        }
        return UNKNOWN;
    }

    public String getName() {
        return this.name;
    }
}
