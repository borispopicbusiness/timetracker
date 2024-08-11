package com.semiramide.timetracker.core.usecase.impl;

import com.semiramide.timetracker.core.entity.Employee;
import com.semiramide.timetracker.core.entity.ProjectEmployee;
import com.semiramide.timetracker.core.service.EmployeeService;
import com.semiramide.timetracker.core.service.ProjectEmployeeService;
import com.semiramide.timetracker.core.usecase.ProjectEmployeeUseCase;
import jakarta.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Builder
@Slf4j
public class ProjectEmployeeUseCaseImpl implements ProjectEmployeeUseCase {

  private final ProjectEmployeeService projectEmployeeService;
  private final EmployeeService employeeService;

  @Override
  @Transactional
  public List<ProjectEmployee> findAll() {
    return projectEmployeeService.findAll();
  }

  @Override
  @Transactional
  public ProjectEmployee save(ProjectEmployee projectEmployee) {
    return projectEmployeeService.save(projectEmployee);
  }

  @Override
  @Transactional
  public void deleteById(UUID id) {
    projectEmployeeService.deleteById(id);
  }

  @Override
  public List<ProjectEmployee> findByLoggedUser(UUID parentId) {
    return projectEmployeeService.findByLoggedUser(parentId);
  }

  @Override
  public List<ProjectEmployee> findByProjectId(UUID projectId) {
    return projectEmployeeService.findByProjectId(projectId);
  }

  @Override
  @Transactional
  public void deleteByProjectIdAndEmployeeId(UUID projectId, UUID employeeId) {
    projectEmployeeService.deleteByProjectIdAndEmployeeId(projectId, employeeId);
  }

  @Override
  public List<Employee> findAssignedEmployees(UUID projectId) {
    List<ProjectEmployee> projectEmployees = projectEmployeeService.findByProjectId(projectId);
    if (projectEmployees.isEmpty()) {
      log.info("No assigned employees for project with ID " + projectId.toString());
      return Collections.emptyList();
    }
    List<UUID> employeesIds =
        projectEmployees.stream().map(e -> e.getEmployeesId()).collect(Collectors.toList());
    return employeeService.findAllEmployeesByIds(employeesIds);
  }

  @Override
  public List<Employee> findNonAssignedEmployees(UUID projectId) {
    List<Employee> nonAssignedEmployees = employeeService.findAllEmployees();
    nonAssignedEmployees.removeAll(findAssignedEmployees(projectId));
    return nonAssignedEmployees;
  }
}
