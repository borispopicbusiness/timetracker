package com.semiramide.timetracker.core.usecase.project;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.semiramide.timetracker.core.entity.Project;
import com.semiramide.timetracker.core.exception.NoProjectFoundException;
import com.semiramide.timetracker.core.repository.ProjectRepository;
import com.semiramide.timetracker.core.service.ProjectService;
import com.semiramide.timetracker.core.service.impl.ProjectServiceImpl;
import com.semiramide.timetracker.core.usecase.ProjectUseCase;
import com.semiramide.timetracker.core.usecase.impl.ProjectUseCaseImpl;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EditProjectTest {
  private ProjectUseCase projectUseCase;
  private ProjectRepository projectRepository;
  private ProjectService projectService;

  private Project oldProject;

  @BeforeEach
  void setUp() {
    projectRepository = mock(ProjectRepository.class);
    projectService = ProjectServiceImpl.builder().projectRepository(projectRepository).build();
    projectUseCase = ProjectUseCaseImpl.builder().projectService(projectService).build();
    oldProject =
        Project.builder()
            .id(UUID.randomUUID())
            .name("Project test")
            .description("this is description test.")
            .build();
  }

  @Test
  @DisplayName("Should update project.")
  // should this be changed
  void should_UpdateProject() throws NoProjectFoundException {
    Project updatedProject =
        Project.builder()
            .id(oldProject.getId())
            .name("updated name")
            .description("This is updated description.")
            .build();

    when(projectRepository.findProjectById(updatedProject.getId()))
        .thenReturn(Optional.ofNullable(oldProject));
    when(projectRepository.updateProject(updatedProject)).thenReturn(updatedProject);
    Project project = projectUseCase.editProject(updatedProject);

    verify(projectRepository).updateProject(updatedProject);
    assertAll(
        () -> assertEquals(updatedProject.getId(), project.getId()),
        () -> assertEquals(updatedProject.getName(), project.getName()),
        () -> assertEquals(updatedProject.getDescription(), project.getDescription()));
  }
}
