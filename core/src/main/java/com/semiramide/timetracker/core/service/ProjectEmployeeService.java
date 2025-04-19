package com.semiramide.timetracker.core.service;

import com.semiramide.timetracker.core.entity.ProjectEmployee;

import java.util.List;
import java.util.UUID;

public interface ProjectEmployeeService {
    List<ProjectEmployee> findAll();

    ProjectEmployee save(ProjectEmployee projectEmployee);

    void deleteById(UUID id);

    void deleteByEmployeeId(UUID employeeId);

    void deleteByProjectIdAndEmployeeId(UUID projectId, UUID employeeId);

    List<ProjectEmployee> findByLoggedUser(UUID parentId);

    List<ProjectEmployee> findByProjectId(UUID projectId);
}
