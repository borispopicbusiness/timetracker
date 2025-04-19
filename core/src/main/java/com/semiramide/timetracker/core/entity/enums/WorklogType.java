package com.semiramide.timetracker.core.entity.enums;

public enum WorklogType {
    DEVELOPMENT("DEVELOPMENT"),
    ARCHITECTURE("ARCHITECTURE"),
    MEETING("MEETING"),
    OTHER("OTHER");

    private final String name;

    WorklogType(String n) {
        this.name = n;
    }

    public static WorklogType fromName(String n) {
        for ( WorklogType wt : WorklogType.values() ) {
            if ( wt.getName().equals(n) ) {
                return wt;
            }
        }
        return OTHER;
    }

    public String getName() {
        return this.name;
    }
}
