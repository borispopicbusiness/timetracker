package com.semiramide.timetracker.application.config;

import com.semiramide.timetracker.core.service.*;
import com.semiramide.timetracker.core.usecase.*;
import com.semiramide.timetracker.core.usecase.impl.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

  @Bean
  NotificationUseCase notificationUseCase(NotificationService notificationService) {
    return NotificationUseCaseImpl.builder().notificationService(notificationService).build();
  }

  @Bean
  LeaveUseCase leaveUseCase(
      LeaveService leaveService,
      EmployeeHierarchyService employeeHierarchyService,
      EmployeeService employeeService) {
    return LeaveUseCaseImpl.builder()
        .leaveService(leaveService)
        .employeeHierarchyService(employeeHierarchyService)
        .employeeService(employeeService)
        .build();
  }

  @Bean
  EmployeeUseCase employeeUseCase(
      EmployeeService employeeService,
      EmployeeHierarchyService employeeHierarchyService,
      LeaveService leaveService,
      WorklogService worklogService,
      ProjectEmployeeService projectEmployeeService) {
    return EmployeeUseCaseImpl.builder()
        .employeeService(employeeService)
        .employeeHierarchyService(employeeHierarchyService)
        .leaveService(leaveService)
        .worklogService(worklogService)
        .projectEmployeeService(projectEmployeeService)
        .build();
  }

  @Bean
  WorklogUseCase worklogUseCase(
      WorklogService worklogService,
      EmployeeService employeeService,
      ProjectService projectService,
      EmployeeHierarchyService employeeHierarchyService) {
    return WorklogUseCaseImpl.builder()
        .worklogService(worklogService)
        .employeeService(employeeService)
        .projectService(projectService)
        .employeeHierarchyService(employeeHierarchyService)
        .build();
  }

  @Bean
  ProjectUseCase projectUseCase(ProjectService projectService) {
    return ProjectUseCaseImpl.builder().projectService(projectService).build();
  }

  @Bean
  ProjectEmployeeUseCase projectEmployeeUseCase(
      ProjectEmployeeService projectEmployeeService, EmployeeService employeeService) {
    return ProjectEmployeeUseCaseImpl.builder()
        .projectEmployeeService(projectEmployeeService)
        .employeeService(employeeService)
        .build();
  }
}
