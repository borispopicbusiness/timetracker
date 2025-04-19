package com.semiramide.timetracker.core.usecase.employee;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.semiramide.timetracker.core.entity.Employee;
import com.semiramide.timetracker.core.event.AppEventPublisher;
import com.semiramide.timetracker.core.event.event.EmployeeCreatedEvent;
import com.semiramide.timetracker.core.exception.EmailAlreadyExistsException;
import com.semiramide.timetracker.core.hierarchy.EmployeeHierarchyGraph;
import com.semiramide.timetracker.core.hierarchy.impl.GuavaMutableGraph;
import com.semiramide.timetracker.core.repository.EmployeeHierarchyRepository;
import com.semiramide.timetracker.core.repository.EmployeeRepository;
import com.semiramide.timetracker.core.service.EmployeeHierarchyService;
import com.semiramide.timetracker.core.service.EmployeeService;
import com.semiramide.timetracker.core.service.impl.EmployeeHierarchyServiceImpl;
import com.semiramide.timetracker.core.service.impl.EmployeeServiceImpl;
import com.semiramide.timetracker.core.usecase.EmployeeUseCase;
import com.semiramide.timetracker.core.usecase.impl.EmployeeUseCaseImpl;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CreateEmployeeTest {
  private EmployeeUseCase employeeUseCase;
  private EmployeeService employeeService;
  private EmployeeHierarchyService employeeHierarchyService;
  private EmployeeHierarchyGraph employeeHierarchyGraph;
  private AppEventPublisher eventPublisher;
  private EmployeeRepository employeeRepository;
  private EmployeeHierarchyRepository employeeHierarchyRepository;

  @BeforeEach
  void setUp() {
    eventPublisher = mock(AppEventPublisher.class);
    employeeRepository = mock(EmployeeRepository.class);
    employeeHierarchyRepository = mock(EmployeeHierarchyRepository.class);

    employeeHierarchyGraph = new GuavaMutableGraph();

    employeeHierarchyService =
        EmployeeHierarchyServiceImpl.builder()
            .employeeHierarchyRepository(employeeHierarchyRepository)
            .employeeHierarchyGraph(employeeHierarchyGraph)
            .build();

    employeeService =
        EmployeeServiceImpl.builder()
            .employeeRepository(employeeRepository)
            .eventPublisher(eventPublisher)
            .employeeHierarchyGraph(employeeHierarchyGraph)
            .build();

    employeeUseCase =
        EmployeeUseCaseImpl.builder()
            .employeeService(employeeService)
            .employeeHierarchyService(employeeHierarchyService)
            .build();
  }

  @Test
  @Disabled
  @DisplayName(value = "Create employee - happy path")
  void shouldCreateEmployeeWhenAllConditionsAreMet() throws EmailAlreadyExistsException {
    UUID id = UUID.randomUUID();
    String email = "email@example.com";
    String firstName = "firstName";
    String lastName = "lastName";
    String password = "password";

    Employee newEmployeeData =
        Employee.builder()
            .email(email)
            .firstName(firstName)
            .lastName(lastName)
            .password(password)
            .build();
    Employee expectedCreatedEmployee =
        Employee.builder()
            .id(id)
            .email(email)
            .firstName(firstName)
            .lastName(lastName)
            .password(password)
            .build();
    Optional<Employee> expectedCreatedEmployeeOptional = Optional.of(expectedCreatedEmployee);
    when(employeeRepository.findEmployeeByEmail(newEmployeeData.getEmail()))
        .thenReturn(Optional.empty());
    when(employeeRepository.saveEmployee(newEmployeeData)).thenReturn(expectedCreatedEmployee);

    // when
    Employee actualCreatedEmployeeOptional = employeeUseCase.createEmployee(newEmployeeData);

    // then
    verify(employeeRepository).findEmployeeByEmail(newEmployeeData.getEmail());
    verify(employeeRepository).saveEmployee(newEmployeeData);
    assertEquals(expectedCreatedEmployeeOptional.get(), actualCreatedEmployeeOptional);
    verify(eventPublisher)
        .publishEvent(
            EmployeeCreatedEvent.builder().employee(actualCreatedEmployeeOptional).build());
  }

  @Test
  @DisplayName(value = "Email is taken")
  void should_ThrowException_When_EmailIsTaken() {
    // given
    String email = "email@example.com";
    String firstName = "firstName";
    String lastName = "lastName";
    String password = "password";

    Employee newEmployeeData =
        Employee.builder()
            .email(email)
            .firstName(firstName)
            .lastName(lastName)
            .password(password)
            .build();

    Employee existingEmployee =
        Employee.builder()
            .id(UUID.randomUUID())
            .email(email)
            .firstName("existing" + firstName)
            .lastName("existing" + lastName)
            .password("existing" + password)
            .build();
    Optional<Employee> existingEmployeeOptional = Optional.of(existingEmployee);
    when(employeeRepository.findEmployeeByEmail(newEmployeeData.getEmail()))
        .thenReturn(existingEmployeeOptional);

    // when

    // then
    assertAll(
        () ->
            assertThrows(
                EmailAlreadyExistsException.class,
                () -> employeeUseCase.createEmployee(newEmployeeData)));
  }
}
