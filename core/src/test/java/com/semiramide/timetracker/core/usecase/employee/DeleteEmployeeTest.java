package com.semiramide.timetracker.core.usecase.employee;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import com.semiramide.timetracker.core.entity.Employee;
import com.semiramide.timetracker.core.event.AppEventPublisher;
import com.semiramide.timetracker.core.event.event.AllEmployeeHierarchyEntriesDeletedForEmployeeEvent;
import com.semiramide.timetracker.core.event.event.EmployeeDeletedEvent;
import com.semiramide.timetracker.core.event.event.SubordinateAssignedEvent;
import com.semiramide.timetracker.core.exception.EmployeeNotFoundException;
import com.semiramide.timetracker.core.hierarchy.EmployeeHierarchyGraph;
import com.semiramide.timetracker.core.hierarchy.impl.GuavaMutableGraph;
import com.semiramide.timetracker.core.repository.*;
import com.semiramide.timetracker.core.repository.*;
import com.semiramide.timetracker.core.security.SecurityProvider;
import com.semiramide.timetracker.core.service.*;
import com.semiramide.timetracker.core.service.impl.*;
import com.semiramide.timetracker.core.usecase.EmployeeUseCase;
import com.semiramide.timetracker.core.usecase.impl.EmployeeUseCaseImpl;
import com.semiramide.timetracker.core.usecase.util.Helper;

import java.util.List;
import java.util.Optional;

import com.semiramide.timetracker.core.service.*;
import com.semiramide.timetracker.core.service.impl.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.stubbing.Answer;

class DeleteEmployeeTest {
    private Helper helper;
    private EmployeeUseCase useCase;
    private LeaveService leaveService;
    private WorklogService worklogService;
    private ProjectEmployeeService projectEmployeeService;
    private EmployeeService employeeService;
    private EmployeeHierarchyService employeeHierarchyService;
    private EmployeeHierarchyGraph graph;
    private WorklogRepository worklogRepository;
    private ProjectEmployeeRepository projectEmployeeRepository;
    private LeaveRepository leaveRepository;
    private SecurityProvider securityProvider;
    private AppEventPublisher eventPublisher;
    private EmployeeRepository employeeRepository;
    private EmployeeHierarchyRepository employeeHierarchyRepository;

    @BeforeEach
    void setUp() {
        helper = new Helper();
        worklogRepository = mock(WorklogRepository.class);
        projectEmployeeRepository = mock(ProjectEmployeeRepository.class);
        leaveRepository = mock(LeaveRepository.class);
        securityProvider = mock(SecurityProvider.class);
        eventPublisher = mock(AppEventPublisher.class);
        employeeRepository = mock(EmployeeRepository.class);
        employeeHierarchyRepository = mock(EmployeeHierarchyRepository.class);

        graph = new GuavaMutableGraph();
        employeeService =
                EmployeeServiceImpl.builder()
                        .employeeHierarchyGraph(graph)
                        .eventPublisher(eventPublisher)
                        .employeeRepository(employeeRepository)
                        .securityProvider(securityProvider)
                        .build();
        leaveService = LeaveServiceImpl.builder().leaveRepository(leaveRepository).build();
        worklogService = WorklogServiceImpl.builder().worklogRepository(worklogRepository).build();
        projectEmployeeService =
                ProjectEmployeeServiceImpl.builder()
                        .projectEmployeeRepository(projectEmployeeRepository)
                        .build();
        employeeHierarchyService =
                EmployeeHierarchyServiceImpl.builder()
                        .employeeHierarchyGraph(graph)
                        .employeeHierarchyRepository(employeeHierarchyRepository)
                        .employeeService(employeeService)
                        .eventPublisher(eventPublisher)
                        .build();

        useCase =
                EmployeeUseCaseImpl.builder()
                        .employeeHierarchyService(employeeHierarchyService)
                        .employeeService(employeeService)
                        .leaveService(leaveService)
                        .worklogService(worklogService)
                        .projectEmployeeService(projectEmployeeService)
                        .build();
    }

    @Test
    @DisplayName(value = "Should delete employee from DB and rearrange graph accordingly")
    void shouldDeleteEmployee() throws EmployeeNotFoundException {
        List<Employee> employees = helper.generateEmployeesList();
        helper.initializeStartingGraph(graph, employees);

        when(employeeRepository.findEmployeeById(employees.get(1).getId()))
                .thenReturn(Optional.of(employees.get(1)));
        when(employeeHierarchyRepository.findAllEmployeeHierarchyEntriesByEmployeeId(
                employees.get(1).getId()))
                .thenReturn(helper.getEdges(employees));
        Answer<Void> answer =
                i -> {
                    graph.deleteNode(((EmployeeDeletedEvent) i.getArgument(0)).getEmployee());
                    return null;
                };
        doAnswer(answer)
                .when(eventPublisher)
                .publishEvent(EmployeeDeletedEvent.builder().employee(employees.get(1)).build());
        ArgumentCaptor<Object> argument = ArgumentCaptor.forClass(Object.class);

        EmployeeHierarchyGraph expectedGraph = new GuavaMutableGraph();
        helper.initializeExpectedGraph(expectedGraph, employees);
        useCase.removeEmployee(employees.get(1).getId());

        verify(eventPublisher, times(3)).publishEvent(argument.capture());
        List<Object> events = argument.getAllValues();
        assertTrue(
                events.contains(
                        AllEmployeeHierarchyEntriesDeletedForEmployeeEvent.builder()
                                .employeeId(employees.get(1).getId())
                                .build()));
        assertTrue(events.contains(EmployeeDeletedEvent.builder().employee(employees.get(1)).build()));
        assertTrue(
                events.contains(
                        SubordinateAssignedEvent.builder()
                                .parent(employees.get(0))
                                .child(employees.get(3))
                                .build()));
        verify(securityProvider).deletePrincipal(employees.get(1).getPrincipalId());
        verify(worklogRepository).deleteByEmployeeId(employees.get(1).getId());
        verify(projectEmployeeRepository).deleteByEmployeeId(employees.get(1).getId());
        verify(leaveRepository).deleteByEmployeeId(employees.get(1).getId());
        verify(employeeHierarchyRepository)
                .deleteEmployeeHierarchyEntriesByEmployeeId(employees.get(1).getId());
        verify(employeeHierarchyRepository, times(1)).saveEmployeeHierarchyEntry(any());
        verify(employeeRepository, times(1)).deleteEmployeeById(employees.get(1).getId());
    }
}
