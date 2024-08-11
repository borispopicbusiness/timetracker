package com.semiramide.timetracker.core.event.event;

import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AllEmployeeHierarchyEntriesDeletedForEmployeeEvent {

  private UUID employeeId;
}
