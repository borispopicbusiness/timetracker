package com.semiramide.timetracker.core.usecase.worklog;

import static org.mockito.Mockito.*;

import com.semiramide.timetracker.core.entity.Worklog;
import com.semiramide.timetracker.core.exception.EmployeeNotFoundException;
import com.semiramide.timetracker.core.repository.EmployeeRepository;
import com.semiramide.timetracker.core.repository.ProjectRepository;
import com.semiramide.timetracker.core.repository.WorklogRepository;
import com.semiramide.timetracker.core.service.EmployeeService;
import com.semiramide.timetracker.core.service.ProjectService;
import com.semiramide.timetracker.core.service.WorklogService;
import com.semiramide.timetracker.core.service.impl.EmployeeServiceImpl;
import com.semiramide.timetracker.core.service.impl.ProjectServiceImpl;
import com.semiramide.timetracker.core.service.impl.WorklogServiceImpl;
import com.semiramide.timetracker.core.usecase.WorklogUseCase;
import com.semiramide.timetracker.core.usecase.impl.WorklogUseCaseImpl;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class GetOwnWorklogsTest {

  private WorklogUseCase worklogUseCase;

  private WorklogService worklogService;

  private WorklogRepository worklogRepository;

  private EmployeeRepository employeeRepository;

  private ProjectRepository projectRepository;

  private EmployeeService employeeService;

  private ProjectService projectService;

  @BeforeEach
  void setUp() {
    worklogRepository = mock(WorklogRepository.class);
    projectRepository = mock(ProjectRepository.class);
    employeeRepository = mock(EmployeeRepository.class);

    worklogService = WorklogServiceImpl.builder().worklogRepository(worklogRepository).build();

    projectService = ProjectServiceImpl.builder().projectRepository(projectRepository).build();

    employeeService = EmployeeServiceImpl.builder().employeeRepository(employeeRepository).build();
    worklogUseCase =
        WorklogUseCaseImpl.builder()
            .worklogService(worklogService)
            .projectService(projectService)
            .employeeService(employeeService)
            .build();
  }

  @Test
  @DisplayName("Should return worklogs of logged employee")
  void should_ReturnOwnWorklogs() throws EmployeeNotFoundException {
    List<Worklog> ownWorklogs = List.of(new Worklog(), new Worklog());
    UUID loggedUser = UUID.randomUUID();
    when(worklogUseCase.getOwnWorklogs(loggedUser)).thenReturn(ownWorklogs);

    worklogUseCase.getOwnWorklogs(loggedUser);

    verify(worklogRepository).findWorklogsByEmployeeId(loggedUser);
  }
}