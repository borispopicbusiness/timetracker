package com.semiramide.timetracker.core.usecase.project;

import static org.junit.jupiter.api.Assertions.assertThrows;
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

class DeleteProjectTest {
  private ProjectUseCase projectUseCase;
  private ProjectRepository projectRepository;
  private ProjectService projectService;

  private Project project;

  @BeforeEach
  void setUp() {
    projectRepository = mock(ProjectRepository.class);
    projectService = ProjectServiceImpl.builder().projectRepository(projectRepository).build();
    projectUseCase = ProjectUseCaseImpl.builder().projectService(projectService).build();
    project =
        Project.builder()
            .id(UUID.randomUUID())
            .name("Project test")
            .description("this is description test.")
            .build();
  }

  @Test
  @DisplayName("Should delete project with given id.")
  void shouldDeleteProjectWithGivenId() throws NoProjectFoundException {

    when(projectRepository.findProjectById(project.getId())).thenReturn(Optional.of(project));

    projectUseCase.deleteProjectById(project.getId());

    verify(projectRepository).deleteProjectById(project.getId());
  }

  @Test
  @DisplayName("should throw exception when project, with given id, does not exist,")
  void shouldThrowExceptionWhenProjectDoesNotExist() throws NoProjectFoundException {

    doThrow(NoProjectFoundException.class)
        .when(projectRepository)
        .deleteProjectById(project.getId());

    assertThrows(
        NoProjectFoundException.class, () -> projectUseCase.deleteProjectById(project.getId()));

    verify(projectRepository, never()).deleteProjectById(project.getId());
  }
}
