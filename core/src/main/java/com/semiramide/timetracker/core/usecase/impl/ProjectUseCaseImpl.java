package com.semiramide.timetracker.core.usecase.impl;

import com.semiramide.timetracker.core.entity.Project;
import com.semiramide.timetracker.core.exception.NoProjectFoundException;
import com.semiramide.timetracker.core.exception.ProjectNameAlreadyTakenException;
import com.semiramide.timetracker.core.service.ProjectService;
import com.semiramide.timetracker.core.usecase.ProjectUseCase;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Builder
@Slf4j
@RequiredArgsConstructor
@Data
public class ProjectUseCaseImpl implements ProjectUseCase {
    private final ProjectService projectService;

    @Override
    public List<Project> getAllProjects() {
        return projectService.findAllProjects();
    }

    @Override
    @Transactional
    public Project createProject(Project project) throws ProjectNameAlreadyTakenException {
        Optional<Project> optionalProject = projectService.findByName(project.getName());
        if ( optionalProject.isPresent() ) {
            log.warn("There is a project with name: " + project.getName());
            throw new ProjectNameAlreadyTakenException(
                    "There is a project with name: " + project.getName());
        }
        return projectService.saveProject(project);
    }

    @Override
    @Transactional
    public Optional<Project> findProjectById(UUID id) {
        return projectService.findProjectById(id);
    }

    @Override
    @Transactional
    public void deleteProjectById(UUID id) throws NoProjectFoundException {
        Optional<Project> project = projectService.findProjectById(id);
        if ( project.isEmpty() ) {
            log.warn("There is no project with id: " + id);
            throw new NoProjectFoundException("There is no project with id: " + id);
        }
        projectService.deleteProjectById(id);
    }

    @Override
    @Transactional
    public Project editProject(Project project) throws NoProjectFoundException {
        Optional<Project> optionalProject = projectService.findProjectById(project.getId());
        if ( optionalProject.isEmpty() ) {
            log.warn("There is no project with id: " + project.getId());
            throw new NoProjectFoundException("Invalid project id.");
        }
        Optional<Project> projectFoundByName = projectService.findByName(project.getName());
        if ( projectFoundByName.isPresent() && projectFoundByName.get().getId() != project.getId() ) {
            log.warn("There is a project with name: " + project.getName());
            throw new ProjectNameAlreadyTakenException(
                    "There is a project with name: " + project.getName());
        }
        return projectService.updateProject(project);
    }

    @Override
    @Transactional
    public Optional<Project> findByName(String name) {
        return projectService.findByName(name);
    }

    @Override
    public List<Project> findAllProjectsByIds(List<UUID> projectIds) {
        return projectService.findAllProjectsByIds(projectIds);
    }
}
