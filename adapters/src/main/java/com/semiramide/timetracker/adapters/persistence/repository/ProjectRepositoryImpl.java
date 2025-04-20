package com.semiramide.timetracker.adapters.persistence.repository;

import com.semiramide.timetracker.adapters.persistence.dto.ProjectDtoDB;
import com.semiramide.timetracker.adapters.persistence.mapper.ProjectMapperDB;
import com.semiramide.timetracker.adapters.persistence.repository.jpa.ProjectRepositoryJpa;
import com.semiramide.timetracker.core.entity.Project;
import com.semiramide.timetracker.core.repository.ProjectRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Builder
@RequiredArgsConstructor
public class ProjectRepositoryImpl implements ProjectRepository {

    private final ProjectRepositoryJpa projectRepositoryJpa;

    @Override
    public List<Project> findAllProjects() {
        return ProjectMapperDB.INSTANCE.projectDtoDBListToProjectList(projectRepositoryJpa.findAll());
    }

    @Override
    public Project saveProject(Project project) {
        ProjectDtoDB projectDtoDB =
                projectRepositoryJpa.save(ProjectMapperDB.INSTANCE.projectToProjectDtoDB(project));
        return ProjectMapperDB.INSTANCE.projectDtoDBToProject(projectDtoDB);
    }

    @Override
    public Optional<Project> findProjectById(UUID id) {
        return Optional.ofNullable(
                ProjectMapperDB.INSTANCE.projectDtoDBToProject(
                        projectRepositoryJpa.findById(id).orElse(null)));
    }

    @Override
    public void deleteProjectById(UUID id) {
        projectRepositoryJpa.deleteById(id);
    }

    @Override
    public Project updateProject(Project project) {
        ProjectDtoDB updatedProject =
                projectRepositoryJpa.saveAndFlush(ProjectMapperDB.INSTANCE.projectToProjectDtoDB(project));
        return ProjectMapperDB.INSTANCE.projectDtoDBToProject(updatedProject);
    }

    @Override
    public Optional<Project> findByName(String name) {
        return Optional.ofNullable(
                ProjectMapperDB.INSTANCE.projectDtoDBToProject(
                        projectRepositoryJpa.findByName(name).orElse(null)));
    }

    @Override
    public List<Project> findAllProjectsByIds(List<UUID> projectIds) {
        return ProjectMapperDB.INSTANCE.projectDtoDBListToProjectList(
                projectRepositoryJpa.findAllByIdIn(projectIds));
    }
}
