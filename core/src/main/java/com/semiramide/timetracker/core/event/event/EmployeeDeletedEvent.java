package com.semiramide.timetracker.core.event.event;

import com.semiramide.timetracker.core.entity.Employee;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmployeeDeletedEvent {

  private Employee employee;
}
