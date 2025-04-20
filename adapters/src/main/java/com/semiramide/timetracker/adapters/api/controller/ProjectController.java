package com.semiramide.timetracker.adapters.api.controller;

import com.semiramide.timetracker.adapters.api.dto.ProjectDtoAPI;
import com.semiramide.timetracker.adapters.api.mapper.ProjectMapperAPI;
import com.semiramide.timetracker.core.entity.Project;
import com.semiramide.timetracker.core.exception.NoProjectFoundException;
import com.semiramide.timetracker.core.exception.ProjectNameAlreadyTakenException;
import com.semiramide.timetracker.core.usecase.EmployeeUseCase;
import com.semiramide.timetracker.core.usecase.ProjectUseCase;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectUseCase projectUseCase;
    private final EmployeeUseCase employeeUseCase;

    @GetMapping()
    public List<ProjectDtoAPI> findAll() {
        return ProjectMapperAPI.INSTANCE.projectListToProjectDtoAPIList(
                projectUseCase.getAllProjects());
    }

    @GetMapping("/{id}")
    public ProjectDtoAPI getById(@PathVariable(name = "id") UUID id) throws NoProjectFoundException {
        Optional<Project> project = projectUseCase.findProjectById(id);
        if ( project.isPresent() ) {
            return ProjectMapperAPI.INSTANCE.projectToProjectDtoAPI(project.get());
        }
        return null;
    }

    @PostMapping
    public ProjectDtoAPI createProject(@RequestBody ProjectDtoAPI project)
            throws ProjectNameAlreadyTakenException, NoProjectFoundException {
        Project savedProject =
                projectUseCase.createProject(ProjectMapperAPI.INSTANCE.projectDtoAPIToProject(project));
        return ProjectMapperAPI.INSTANCE.projectToProjectDtoAPI(savedProject);
    }

    @PutMapping
    public ProjectDtoAPI editProject(@RequestBody ProjectDtoAPI project)
            throws NoProjectFoundException {
        Project editedProject =
                projectUseCase.editProject(ProjectMapperAPI.INSTANCE.projectDtoAPIToProject(project));
        return ProjectMapperAPI.INSTANCE.projectToProjectDtoAPI(editedProject);
    }

    @DeleteMapping("/{id}")
    public void deleteProject(@PathVariable(name = "id") UUID id) throws NoProjectFoundException {
        projectUseCase.deleteProjectById(id);
    }
}
