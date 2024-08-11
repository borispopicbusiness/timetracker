package com.semiramide.timetracker.core.usecase.worklog;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.semiramide.timetracker.core.entity.Employee;
import com.semiramide.timetracker.core.entity.Project;
import com.semiramide.timetracker.core.entity.Worklog;
import com.semiramide.timetracker.core.exception.EmployeeNotFoundException;
import com.semiramide.timetracker.core.exception.InvalidArgumentException;
import com.semiramide.timetracker.core.exception.NoProjectFoundException;
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

class CreateWorklogTest {

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
  }

  @Test
  @DisplayName("Should create worklog.")
  void should_CreateWorklog()
      throws NoProjectFoundException, EmployeeNotFoundException, InvalidArgumentException {
    // conditions go below
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

    when(projectRepository.findProjectById(worklog.getProjectId()))
        .thenReturn(Optional.ofNullable(Project.builder().build()));
    when(employeeRepository.findEmployeeById(worklog.getEmployeeId()))
        .thenReturn(Optional.ofNullable(Employee.builder().build()));

    // assertTrue(check(worklog.getStartTime(),worklog.getEndTime(),worklog.getTotalTime()));

    worklogUseCase.addWorklog(worklog);

    verify(worklogRepository).saveWorklog(worklog);
  }

  //    boolean check(LocalDateTime start, LocalDateTime end, Long total){
  //        if(start == null || end == null){
  //            if(total != null){
  //                return true;
  //            }
  //            return false;
  //        }
  //        worklog.setTotalTime(Integer.toUnsignedLong(end.getNano()-start.getNano()));
  //        return true;
  //    }
  @Test
  @DisplayName("Should not create worklog when start time, end time and total time are null")
  void should_NotCreateWorklog_When_dataIsNotValid() {
    worklog =
        Worklog.builder()
            .id(UUID.randomUUID())
            .employeeId(UUID.randomUUID())
            .taskName("WorklogTest")
            .description("This is test worklog.")
            .startTime(null)
            .endTime(null)
            .totalTime(null)
            .creationDate(LocalDate.now())
            .projectId(UUID.randomUUID())
            .build();
    when(projectRepository.findProjectById(worklog.getProjectId()))
        .thenReturn(Optional.ofNullable(Project.builder().build()));
    when(employeeRepository.findEmployeeById(worklog.getEmployeeId()))
        .thenReturn(Optional.ofNullable(Employee.builder().build()));

    assertThrows(InvalidArgumentException.class, () -> worklogUseCase.addWorklog(worklog));

    verify(worklogRepository, never()).saveWorklog(worklog);
  }

  @Test
  @DisplayName(
      "Should create worklog when start time and end time are null, and total time is not null")
  void should_CreateWorklog_When_OnlyTotalTimeIsNotNull()
      throws NoProjectFoundException, EmployeeNotFoundException, InvalidArgumentException {
    worklog =
        Worklog.builder()
            .id(UUID.randomUUID())
            .employeeId(UUID.randomUUID())
            .taskName("WorklogTest")
            .description("This is test worklog.")
            .startTime(null)
            .endTime(null)
            .totalTime(10.0)
            .creationDate(LocalDate.now())
            .projectId(UUID.randomUUID())
            .build();
    when(projectRepository.findProjectById(worklog.getProjectId()))
        .thenReturn(Optional.ofNullable(Project.builder().build()));
    when(employeeRepository.findEmployeeById(worklog.getEmployeeId()))
        .thenReturn(Optional.ofNullable(Employee.builder().build()));

    worklogUseCase.addWorklog(worklog);

    verify(worklogRepository).saveWorklog(worklog);
  }

  @Test
  @DisplayName(
      "Should create worklog when start time and end time are not null, and total time is  null")
  void should_CreateWorklog_When_OnlyTotalTimeIsNull()
      throws NoProjectFoundException, EmployeeNotFoundException, InvalidArgumentException {
    worklog =
        Worklog.builder()
            .id(UUID.randomUUID())
            .employeeId(UUID.randomUUID())
            .taskName("WorklogTest")
            .description("This is test worklog.")
            .startTime(LocalDateTime.now())
            .endTime(LocalDateTime.now())
            .totalTime(null)
            .creationDate(LocalDate.now())
            .projectId(UUID.randomUUID())
            .build();
    when(projectRepository.findProjectById(worklog.getProjectId()))
        .thenReturn(Optional.ofNullable(Project.builder().build()));
    when(employeeRepository.findEmployeeById(worklog.getEmployeeId()))
        .thenReturn(Optional.ofNullable(Employee.builder().build()));

    worklogUseCase.addWorklog(worklog);

    verify(worklogRepository).saveWorklog(worklog);
  }

  @Test
  @DisplayName("Should create worklog when only start time is null")
  void should_CreateWorklog_When_OnlyStartTimeIsNull()
      throws NoProjectFoundException, EmployeeNotFoundException, InvalidArgumentException {
    worklog =
        Worklog.builder()
            .id(UUID.randomUUID())
            .employeeId(UUID.randomUUID())
            .taskName("WorklogTest")
            .description("This is test worklog.")
            .startTime(null)
            .endTime(LocalDateTime.now())
            .totalTime(10.0)
            .creationDate(LocalDate.now())
            .projectId(UUID.randomUUID())
            .build();
    when(projectRepository.findProjectById(worklog.getProjectId()))
        .thenReturn(Optional.ofNullable(Project.builder().build()));
    when(employeeRepository.findEmployeeById(worklog.getEmployeeId()))
        .thenReturn(Optional.ofNullable(Employee.builder().build()));

    worklogUseCase.addWorklog(worklog);

    verify(worklogRepository).saveWorklog(worklog);
  }

  @Test
  @DisplayName("Should create worklog when only end time is null")
  void should_CreateWorklog_When_OnlyEndTimeIsNull()
      throws NoProjectFoundException, EmployeeNotFoundException, InvalidArgumentException {
    worklog =
        Worklog.builder()
            .id(UUID.randomUUID())
            .employeeId(UUID.randomUUID())
            .taskName("WorklogTest")
            .description("This is test worklog.")
            .startTime(LocalDateTime.now())
            .endTime(null)
            .totalTime(10.0)
            .creationDate(LocalDate.now())
            .projectId(UUID.randomUUID())
            .build();
    when(projectRepository.findProjectById(worklog.getProjectId()))
        .thenReturn(Optional.ofNullable(Project.builder().build()));
    when(employeeRepository.findEmployeeById(worklog.getEmployeeId()))
        .thenReturn(Optional.ofNullable(Employee.builder().build()));

    worklogUseCase.addWorklog(worklog);

    verify(worklogRepository).saveWorklog(worklog);
  }

  @Test
  @DisplayName("Should not worklog when project id is invalid")
  void should_ThrowException_When_ProjectIdIsInvalid() {
    worklog =
        Worklog.builder()
            .id(UUID.randomUUID())
            .employeeId(UUID.randomUUID())
            .taskName("WorklogTest")
            .description("This is test worklog.")
            .startTime(LocalDateTime.now())
            .endTime(LocalDateTime.now())
            .totalTime(10.0)
            .creationDate(LocalDate.now())
            .projectId(UUID.randomUUID())
            .build();
    when(projectRepository.findProjectById(worklog.getProjectId())).thenReturn(Optional.empty());
    when(employeeRepository.findEmployeeById(worklog.getEmployeeId()))
        .thenReturn(Optional.ofNullable(Employee.builder().build()));

    assertThrows(NoProjectFoundException.class, () -> worklogUseCase.addWorklog(worklog));

    verify(worklogRepository, never()).saveWorklog(worklog);
  }

  @Test
  @DisplayName("Should not worklog when employee id is invalid")
  void should_ThrowException_When_EmployeeIdIsInvalid() {
    worklog =
        Worklog.builder()
            .id(UUID.randomUUID())
            .employeeId(UUID.randomUUID())
            .taskName("WorklogTest")
            .description("This is test worklog.")
            .startTime(LocalDateTime.now())
            .endTime(LocalDateTime.now())
            .totalTime(10.0)
            .creationDate(LocalDate.now())
            .projectId(UUID.randomUUID())
            .build();
    when(projectRepository.findProjectById(worklog.getProjectId()))
        .thenReturn(Optional.ofNullable(Project.builder().build()));
    when(employeeRepository.findEmployeeById(worklog.getEmployeeId())).thenReturn(Optional.empty());

    assertThrows(EmployeeNotFoundException.class, () -> worklogUseCase.addWorklog(worklog));

    verify(worklogRepository, never()).saveWorklog(worklog);
  }
}
