package com.semiramide.timetracker.core.usecase.employee;

import static org.mockito.Mockito.*;

import com.semiramide.timetracker.core.entity.Employee;
import com.semiramide.timetracker.core.event.AppEventPublisher;
import com.semiramide.timetracker.core.exception.EmailAlreadyExistsException;
import com.semiramide.timetracker.core.exception.EmployeeNotFoundException;
import com.semiramide.timetracker.core.repository.EmployeeRepository;
import com.semiramide.timetracker.core.security.SecurityProvider;
import com.semiramide.timetracker.core.service.EmployeeService;
import com.semiramide.timetracker.core.service.impl.EmployeeServiceImpl;
import com.semiramide.timetracker.core.usecase.EmployeeUseCase;
import com.semiramide.timetracker.core.usecase.impl.EmployeeUseCaseImpl;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EditEmployeeTest {

  private EmployeeUseCase employeeUseCase;
  private EmployeeService employeeService;

  private EmployeeRepository employeeRepository;
  private SecurityProvider securityProvider;
  private AppEventPublisher eventPublisher;

  @BeforeEach
  void setUp() {
    employeeRepository = mock(EmployeeRepository.class);
    securityProvider = mock(SecurityProvider.class);
    eventPublisher = mock(AppEventPublisher.class);

    employeeService =
        EmployeeServiceImpl.builder()
            .employeeRepository(employeeRepository)
            .securityProvider(securityProvider)
            .eventPublisher(eventPublisher)
            .build();
    employeeUseCase = EmployeeUseCaseImpl.builder().employeeService(employeeService).build();
  }

  @Test
  void shouldEditEmployee() throws EmailAlreadyExistsException, EmployeeNotFoundException {
    UUID employeeId = UUID.randomUUID();

    Employee expectedEmployee =
        Employee.builder()
            .id(employeeId)
            .firstName("fName1")
            .lastName("lName1")
            .email("email1")
            .build();

    when(employeeRepository.findEmployeeById(employeeId)).thenReturn(Optional.of(expectedEmployee));
    employeeUseCase.updateEmployee(expectedEmployee);

    verify(employeeRepository).saveEmployee(expectedEmployee);
  }
}
