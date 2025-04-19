package com.semiramide.timetracker.core.event.event;

import com.semiramide.timetracker.core.entity.EmployeeHierarchyEntry;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SubordinateUnassignedEvent {
    private EmployeeHierarchyEntry entry;
}
