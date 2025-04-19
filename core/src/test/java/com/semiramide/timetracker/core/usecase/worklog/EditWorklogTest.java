package com.semiramide.timetracker.core.usecase.worklog;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.semiramide.timetracker.core.entity.Employee;
import com.semiramide.timetracker.core.entity.Project;
import com.semiramide.timetracker.core.entity.Worklog;
import com.semiramide.timetracker.core.exception.EmployeeNotFoundException;
import com.semiramide.timetracker.core.exception.InvalidArgumentException;
import com.semiramide.timetracker.core.exception.NoProjectFoundException;
import com.semiramide.timetracker.core.exception.WorklogNotFoundException;
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

class EditWorklogTest {
  private WorklogUseCase worklogUseCase;
  private WorklogService worklogService;
  private WorklogRepository worklogRepository;
  private Worklog worklog;
  private Worklog updatedWorklog;
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

    worklogUseCase = WorklogUseCaseImpl.builder()
            .worklogService(worklogService)
            .projectService(projectService)
            .employeeService(employeeService)
            .build();

    worklog = Worklog.builder()
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

    updatedWorklog = Worklog.builder()
            .id(worklog.getId())
            .taskName("Updated worklog")
            .description("This is updated worklog")
            .startTime(LocalDateTime.now())
            .endTime(LocalDateTime.now())
            .totalTime(9.0)
            .creationDate(LocalDate.now())
            .projectId(worklog.getProjectId())
            .employeeId(worklog.getEmployeeId())
            .build();
  }

  @Test
  @DisplayName("Should edit worklog.")
  void shouldEditWorklog()
      throws NoProjectFoundException,
          EmployeeNotFoundException,
          WorklogNotFoundException,
          InvalidArgumentException {

    when(worklogRepository.findWorklogById(worklog.getId()))
        .thenReturn(Optional.ofNullable(worklog));
    when(projectRepository.findProjectById(worklog.getProjectId()))
        .thenReturn(Optional.ofNullable(Project.builder().build()));
    when(employeeRepository.findEmployeeById(worklog.getEmployeeId()))
        .thenReturn(Optional.ofNullable(Employee.builder().build()));

    worklogUseCase.updateWorklog(updatedWorklog);

    assertAll(
        () -> worklog.getTaskName().contentEquals(updatedWorklog.getTaskName()),
        () -> worklog.getDescription().contentEquals(updatedWorklog.getDescription()));

    verify(worklogRepository).saveWorklog(updatedWorklog);
  }

  @Test
  @DisplayName("Should not update worklog if worklogId is invalid.")
  void shouldNotUpdateWorklogWhenWorklogIdIsInvalid() {

    when(worklogRepository.findWorklogById(worklog.getId())).thenReturn(Optional.empty());
    when(projectRepository.findProjectById(worklog.getProjectId()))
        .thenReturn(Optional.ofNullable(Project.builder().build()));
    when(employeeRepository.findEmployeeById(worklog.getEmployeeId()))
        .thenReturn(Optional.ofNullable(Employee.builder().build()));

    assertThrows(
        WorklogNotFoundException.class, () -> worklogUseCase.updateWorklog(updatedWorklog));

    verify(worklogRepository, never()).saveWorklog(updatedWorklog);
  }

  @Test
  @DisplayName("Should not update worklog if employee id is invalid.")
  void shouldNotUpdateWorklogWhenEmployeeIdIsInvalid() {

    when(worklogRepository.findWorklogById(worklog.getId()))
        .thenReturn(Optional.ofNullable(worklog));
    when(projectRepository.findProjectById(worklog.getProjectId()))
        .thenReturn(Optional.ofNullable(Project.builder().build()));
    when(employeeRepository.findEmployeeById(worklog.getEmployeeId())).thenReturn(Optional.empty());

    assertThrows(
        EmployeeNotFoundException.class, () -> worklogUseCase.updateWorklog(updatedWorklog));

    verify(worklogRepository, never()).saveWorklog(updatedWorklog);
  }

  @Test
  @DisplayName("Should not update worklog if project id is invalid.")
  void shouldNotUpdateWorklogWhenProjectIdIsInvalid() {

    when(worklogRepository.findWorklogById(worklog.getId()))
        .thenReturn(Optional.ofNullable(worklog));
    when(projectRepository.findProjectById(worklog.getProjectId())).thenReturn(Optional.empty());
    when(employeeRepository.findEmployeeById(worklog.getEmployeeId()))
        .thenReturn(Optional.ofNullable(Employee.builder().build()));

    assertThrows(NoProjectFoundException.class, () -> worklogUseCase.updateWorklog(updatedWorklog));

    verify(worklogRepository, never()).saveWorklog(updatedWorklog);
  }

  @Test
  @DisplayName("Should not update worklog when start time, end time, and total time are null.")
  void shouldNotUpdateWorklogWhenDataIsInvalid() {

    updatedWorklog.setStartTime(null);
    updatedWorklog.setEndTime(null);
    updatedWorklog.setTotalTime(null);

    when(worklogRepository.findWorklogById(worklog.getId()))
        .thenReturn(Optional.ofNullable(worklog));
    when(projectRepository.findProjectById(worklog.getProjectId()))
        .thenReturn(Optional.ofNullable(Project.builder().build()));
    when(employeeRepository.findEmployeeById(worklog.getEmployeeId()))
        .thenReturn(Optional.ofNullable(Employee.builder().build()));

    assertThrows(
        InvalidArgumentException.class, () -> worklogUseCase.updateWorklog(updatedWorklog));

    verify(worklogRepository, never()).saveWorklog(updatedWorklog);
  }

  @Test
  @DisplayName("Should  update worklog when only start time is null.")
  void shouldUpdateWorklogWhenOnlyStartTimeIsNull()
      throws NoProjectFoundException,
          WorklogNotFoundException,
          EmployeeNotFoundException,
          InvalidArgumentException {

    updatedWorklog.setStartTime(null);
    updatedWorklog.setEndTime(LocalDateTime.now());
    updatedWorklog.setTotalTime(9.0);

    when(worklogRepository.findWorklogById(worklog.getId()))
        .thenReturn(Optional.ofNullable(worklog));
    when(projectRepository.findProjectById(worklog.getProjectId()))
        .thenReturn(Optional.ofNullable(Project.builder().build()));
    when(employeeRepository.findEmployeeById(worklog.getEmployeeId()))
        .thenReturn(Optional.ofNullable(Employee.builder().build()));

    worklogUseCase.updateWorklog(updatedWorklog);

    verify(worklogRepository).saveWorklog(updatedWorklog);
  }

  @Test
  @DisplayName("Should  update worklog when only end time is null.")
  void shouldUpdateWorklogWhenOnlyEndTimeIsNull()
      throws NoProjectFoundException,
          WorklogNotFoundException,
          EmployeeNotFoundException,
          InvalidArgumentException {

    updatedWorklog.setStartTime(LocalDateTime.now());
    updatedWorklog.setEndTime(null);
    updatedWorklog.setTotalTime(9.0);

    when(worklogRepository.findWorklogById(worklog.getId()))
        .thenReturn(Optional.ofNullable(worklog));
    when(projectRepository.findProjectById(worklog.getProjectId()))
        .thenReturn(Optional.ofNullable(Project.builder().build()));
    when(employeeRepository.findEmployeeById(worklog.getEmployeeId()))
        .thenReturn(Optional.ofNullable(Employee.builder().build()));

    worklogUseCase.updateWorklog(updatedWorklog);

    verify(worklogRepository).saveWorklog(updatedWorklog);
  }

  @Test
  @DisplayName("Should update worklog when only total time is null")
  void shouldUpdateWorklogWhenOnlyTotalTimeIsNull()
      throws NoProjectFoundException,
          WorklogNotFoundException,
          EmployeeNotFoundException,
          InvalidArgumentException {

    updatedWorklog.setStartTime(LocalDateTime.now());
    updatedWorklog.setEndTime(LocalDateTime.now());
    updatedWorklog.setTotalTime(null);

    when(worklogRepository.findWorklogById(worklog.getId()))
        .thenReturn(Optional.ofNullable(worklog));
    when(projectRepository.findProjectById(worklog.getProjectId()))
        .thenReturn(Optional.ofNullable(Project.builder().build()));
    when(employeeRepository.findEmployeeById(worklog.getEmployeeId()))
        .thenReturn(Optional.ofNullable(Employee.builder().build()));

    worklogUseCase.updateWorklog(updatedWorklog);

    verify(worklogRepository).saveWorklog(updatedWorklog);
  }

  @Test
  @DisplayName("Should update worklog when only total time is not null")
  void shouldUpdateWorklogWhenOnlyTotalTimeIsNotNull()
      throws NoProjectFoundException,
          WorklogNotFoundException,
          EmployeeNotFoundException,
          InvalidArgumentException {

    updatedWorklog.setStartTime(null);
    updatedWorklog.setEndTime(null);
    updatedWorklog.setTotalTime(9.0);

    when(worklogRepository.findWorklogById(worklog.getId()))
        .thenReturn(Optional.ofNullable(worklog));
    when(projectRepository.findProjectById(worklog.getProjectId()))
        .thenReturn(Optional.ofNullable(Project.builder().build()));
    when(employeeRepository.findEmployeeById(worklog.getEmployeeId()))
        .thenReturn(Optional.ofNullable(Employee.builder().build()));

    worklogUseCase.updateWorklog(updatedWorklog);

    verify(worklogRepository).saveWorklog(updatedWorklog);
  }
}
