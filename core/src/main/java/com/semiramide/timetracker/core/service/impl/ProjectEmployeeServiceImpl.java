package com.semiramide.timetracker.core.service.impl;

import com.semiramide.timetracker.core.entity.ProjectEmployee;
import com.semiramide.timetracker.core.repository.ProjectEmployeeRepository;
import com.semiramide.timetracker.core.service.ProjectEmployeeService;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Builder
public class ProjectEmployeeServiceImpl implements ProjectEmployeeService {

  private final ProjectEmployeeRepository projectEmployeeRepository;

  @Override
  public List<ProjectEmployee> findAll() {
    return projectEmployeeRepository.findAll();
  }

  @Override
  public ProjectEmployee save(ProjectEmployee projectEmployee) {
    return projectEmployeeRepository.save(projectEmployee);
  }

  @Override
  public void deleteById(UUID id) {
    projectEmployeeRepository.deleteById(id);
  }

  @Override
  public void deleteByEmployeeId(UUID employeeId) {
    projectEmployeeRepository.deleteByEmployeeId(employeeId);
  }

  @Override
  public List<ProjectEmployee> findByLoggedUser(UUID parentId) {
    return projectEmployeeRepository.findByEmployeesId(parentId);
  }

  @Override
  public List<ProjectEmployee> findByProjectId(UUID projectId) {
    return projectEmployeeRepository.findByProjectId(projectId);
  }

  @Override
  public void deleteByProjectIdAndEmployeeId(UUID projectId, UUID employeeId) {
    projectEmployeeRepository.deleteByProjectIdAndEmployeeId(projectId, employeeId);
  }
}
