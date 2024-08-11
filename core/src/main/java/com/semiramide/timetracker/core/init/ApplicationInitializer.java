package com.semiramide.timetracker.core.init;

import com.semiramide.timetracker.core.security.SecurityProvider;
import com.semiramide.timetracker.core.service.EmployeeHierarchyService;
import jakarta.annotation.PostConstruct;
import lombok.Builder;

@Builder
public class ApplicationInitializer {

  private EmployeeHierarchyService employeeHierarchyService;
  private SecurityProvider securityProvider;

  @PostConstruct
  public void initialize() {
    employeeHierarchyService.loadGraph();
    securityProvider.initialize();
  }
}
