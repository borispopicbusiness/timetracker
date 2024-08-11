package com.semiramide.timetracker.adapters.persistence.repository;

import com.semiramide.timetracker.adapters.persistence.dto.ProjectEmployeeDtoDB;
import com.semiramide.timetracker.adapters.persistence.mapper.ProjectEmployeeMapperDB;
import com.semiramide.timetracker.adapters.persistence.repository.jpa.ProjectEmployeeRepositoryJpa;
import com.semiramide.timetracker.core.entity.ProjectEmployee;
import com.semiramide.timetracker.core.repository.ProjectEmployeeRepository;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder
public class ProjectEmployeeRepositoryImpl implements ProjectEmployeeRepository {

  private final ProjectEmployeeRepositoryJpa projectEmployeeRepositoryJpa;

  @Override
  public List<ProjectEmployee> findAll() {
    return ProjectEmployeeMapperDB.INSTANCE.projectEmployeeDtoDBListToProjectEmployeeList(
        projectEmployeeRepositoryJpa.findAll());
  }

  @Override
  public ProjectEmployee save(ProjectEmployee projectEmployee) {
    ProjectEmployeeDtoDB savedEntity =
        projectEmployeeRepositoryJpa.save(
            ProjectEmployeeMapperDB.INSTANCE.projectEmployeeToProjectEmployeeDtoDB(
                projectEmployee));
    return ProjectEmployeeMapperDB.INSTANCE.projectEmployeeDtoDBToProjectEmployee(savedEntity);
  }

  @Override
  public void deleteById(UUID id) {
    projectEmployeeRepositoryJpa.deleteById(id);
  }

  @Override
  public void deleteByEmployeeId(UUID employeeId) {
    projectEmployeeRepositoryJpa.deleteByEmployeesId(employeeId);
  }

  @Override
  public List<ProjectEmployee> findByEmployeesId(UUID id) {
    List<ProjectEmployeeDtoDB> subordinates = projectEmployeeRepositoryJpa.findByEmployeesId(id);
    return ProjectEmployeeMapperDB.INSTANCE.projectEmployeeDtoDBListToProjectEmployeeList(
        subordinates);
  }

  @Override
  public List<ProjectEmployee> findByProjectId(UUID id) {
    List<ProjectEmployeeDtoDB> projectsEmployees = projectEmployeeRepositoryJpa.findByProjectId(id);
    return ProjectEmployeeMapperDB.INSTANCE.projectEmployeeDtoDBListToProjectEmployeeList(
        projectsEmployees);
  }

  @Override
  public void deleteByProjectIdAndEmployeeId(UUID projectID, UUID employeeId) {
    projectEmployeeRepositoryJpa.deleteByProjectIdAndEmployeesId(projectID, employeeId);
  }
}
