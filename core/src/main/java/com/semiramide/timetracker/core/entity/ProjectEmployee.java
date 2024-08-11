package com.semiramide.timetracker.core.entity;

import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjectEmployee {

  private UUID id;
  private UUID projectId;
  private UUID employeesId;
}
