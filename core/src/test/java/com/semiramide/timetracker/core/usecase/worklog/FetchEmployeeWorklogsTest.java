package com.semiramide.timetracker.core.usecase.worklog;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.semiramide.timetracker.core.entity.Employee;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FetchEmployeeWorklogsTest {
  private WorklogUseCase worklogUseCase;

  private WorklogService worklogService;

  private WorklogRepository worklogRepository;

  private Worklog worklog;

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

    worklog =
        Worklog.builder()
            .id(UUID.randomUUID())
            .employeeId(UUID.randomUUID())
            .taskName("WorklogTest")
            .description("This is test worklog.")
            .startTime(LocalDateTime.now().minusDays(3))
            .endTime(LocalDateTime.now().minusDays(2))
            .totalTime(10.0)
            .creationDate(LocalDate.now())
            .projectId(UUID.randomUUID())
            .build();
  }

  @Test
  @DisplayName("Should find all worklogs by employee id")
  void shouldFetchEmployeeWorklogs() throws EmployeeNotFoundException {
    Employee employee = Employee.builder().id(UUID.randomUUID()).build();

    when(employeeRepository.findEmployeeById(employee.getId()))
        .thenReturn(Optional.ofNullable(employee));

    worklogUseCase.listEmployeeWorklogs(employee.getId());

    verify(worklogRepository).findWorklogsByEmployeeId(employee.getId());
  }

  @Test
  @DisplayName("Should not find worklogs when employee id is not valid")
  void shouldThrowExceptionWhenEmployeeIdIsNotValid() {
    Employee employee = Employee.builder().id(UUID.randomUUID()).build();

    when(employeeRepository.findEmployeeById(employee.getId())).thenReturn(Optional.empty());

    assertThrows(
        EmployeeNotFoundException.class,
        () -> worklogUseCase.listEmployeeWorklogs(employee.getId()));

    verify(worklogRepository, never()).findWorklogsByEmployeeId(employee.getId());
  }
}
