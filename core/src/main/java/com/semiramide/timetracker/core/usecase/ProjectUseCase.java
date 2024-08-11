package com.semiramide.timetracker.core.usecase;

import com.semiramide.timetracker.core.entity.Project;
import com.semiramide.timetracker.core.exception.NoProjectFoundException;
import com.semiramide.timetracker.core.exception.ProjectNameAlreadyTakenException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProjectUseCase {

  List<Project> getAllProjects();

  Project createProject(Project project)
      throws ProjectNameAlreadyTakenException, NoProjectFoundException;

  Optional<Project> findProjectById(UUID id);

  void deleteProjectById(UUID id) throws NoProjectFoundException;

  Project editProject(Project project) throws NoProjectFoundException;

  Optional<Project> findByName(String name);

  List<Project> findAllProjectsByIds(List<UUID> projectIds);
}
