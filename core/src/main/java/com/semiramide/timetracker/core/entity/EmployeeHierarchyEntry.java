package com.semiramide.timetracker.core.entity;

import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmployeeHierarchyEntry {
    private UUID id;
    private UUID parentId;
    private UUID childId;
}
