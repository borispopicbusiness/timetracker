package com.semiramide.timetracker.core.usecase.employee;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.semiramide.timetracker.core.entity.Employee;
import com.semiramide.timetracker.core.entity.EmployeeHierarchyEntry;
import com.semiramide.timetracker.core.event.AppEventPublisher;
import com.semiramide.timetracker.core.event.event.SubordinateAssignedEvent;
import com.semiramide.timetracker.core.exception.AlreadyASubordinateException;
import com.semiramide.timetracker.core.exception.CycleDetectedException;
import com.semiramide.timetracker.core.exception.EmployeeNotFoundException;
import com.semiramide.timetracker.core.exception.InvalidArgumentException;
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
import com.semiramide.timetracker.core.usecase.util.Helper;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AssignSubordinateTest {

  private Helper helper;

  private EmployeeUseCase employeeUseCase;
  private EmployeeService employeeService;
  private EmployeeHierarchyService employeeHierarchyService;
  private EmployeeHierarchyGraph employeeHierarchyGraph;

  private AppEventPublisher eventPublisher;
  private EmployeeRepository employeeRepository;
  private EmployeeHierarchyRepository employeeHierarchyRepository;

  @BeforeEach
  void setupEach() {
    helper = new Helper();
    eventPublisher = mock(AppEventPublisher.class);
    employeeRepository = mock(EmployeeRepository.class);
    employeeHierarchyRepository = mock(EmployeeHierarchyRepository.class);

    employeeHierarchyGraph = new GuavaMutableGraph();
    employeeService =
        EmployeeServiceImpl.builder()
            .employeeRepository(employeeRepository)
            .eventPublisher(eventPublisher)
            .employeeHierarchyGraph(employeeHierarchyGraph)
            .build();
    employeeHierarchyService =
        EmployeeHierarchyServiceImpl.builder()
            .employeeHierarchyRepository(employeeHierarchyRepository)
            .employeeHierarchyGraph(employeeHierarchyGraph)
            .eventPublisher(eventPublisher)
            .build();
    employeeUseCase =
        EmployeeUseCaseImpl.builder()
            .employeeService(employeeService)
            .employeeHierarchyService(employeeHierarchyService)
            .build();
  }

  @Test
  @DisplayName(value = "Assign subordinate - happy path")
  void shouldAssignSubordinate()
      throws CycleDetectedException,
          EmployeeNotFoundException,
          InvalidArgumentException,
          AlreadyASubordinateException {
    Employee parent = Employee.builder().build();
    Employee child = Employee.builder().build();

    helper.initializeNodesAndAddToGraph(employeeHierarchyGraph, parent, child);

    EmployeeHierarchyEntry employeeHierarchyEntry =
        EmployeeHierarchyEntry.builder().parentId(parent.getId()).childId(child.getId()).build();

    when(employeeRepository.findEmployeeById(parent.getId())).thenReturn(Optional.of(parent));
    when(employeeRepository.findEmployeeById(child.getId())).thenReturn(Optional.of(child));

    employeeUseCase.assignSubordinate(parent.getId(), child.getId());

    verify(employeeHierarchyRepository).saveEmployeeHierarchyEntry(employeeHierarchyEntry);
    verify(eventPublisher)
        .publishEvent(SubordinateAssignedEvent.builder().parent(parent).child(child).build());
  }

  @Test
  void shouldThrowCycleDetectedExceptionWhenCycleDetected() {
    UUID node_1_id = UUID.randomUUID();
    UUID node_2_id = UUID.randomUUID();
    UUID node_3_id = UUID.randomUUID();

    Employee node_1 = Employee.builder().id(node_1_id).email("node1").firstName("node 1").build();
    Employee node_2 = Employee.builder().id(node_2_id).email("node2").firstName("node 2").build();
    Employee node_3 = Employee.builder().id(node_3_id).email("node3").firstName("node 3").build();

    EmployeeHierarchyEntry employeeHierarchyEntry =
        EmployeeHierarchyEntry.builder().parentId(node_3.getId()).childId(node_1.getId()).build();

    employeeHierarchyGraph.addNode(node_1);
    employeeHierarchyGraph.addNode(node_2);
    employeeHierarchyGraph.addNode(node_3);

    employeeHierarchyGraph.putEdge(node_1, node_2);
    employeeHierarchyGraph.putEdge(node_2, node_3);

    when(employeeRepository.findEmployeeById(node_1_id)).thenReturn(Optional.of(node_1));
    when(employeeRepository.findEmployeeById(node_2_id)).thenReturn(Optional.of(node_2));
    when(employeeRepository.findEmployeeById(node_3_id)).thenReturn(Optional.of(node_3));

    assertAll(
        () ->
            assertThrows(
                CycleDetectedException.class,
                () -> employeeUseCase.assignSubordinate(node_3.getId(), node_1.getId())),
        () ->
            verify(employeeHierarchyRepository, never())
                .saveEmployeeHierarchyEntry(employeeHierarchyEntry),
        () -> assertFalse(employeeHierarchyGraph.hasCycle()));
  }

  @Test
  void shouldThrowAlreadyASubordinateExceptionWhenChildAlreadyReachableFromParent() {

    Employee e1 = Employee.builder().build();
    Employee e2 = Employee.builder().build();
    Employee e3 = Employee.builder().build();

    helper.initializeNodesAndAddToGraph(employeeHierarchyGraph, e1, e2, e3);

    employeeHierarchyGraph.putEdge(e1, e2);
    employeeHierarchyGraph.putEdge(e2, e3);

    when(employeeRepository.findEmployeeById(e1.getId())).thenReturn(Optional.of(e1));
    when(employeeRepository.findEmployeeById(e2.getId())).thenReturn(Optional.of(e2));
    when(employeeRepository.findEmployeeById(e3.getId())).thenReturn(Optional.of(e3));

    assertAll(
        () ->
            assertThrows(
                AlreadyASubordinateException.class,
                () -> employeeUseCase.assignSubordinate(e1.getId(), e3.getId())),
        () -> verify(employeeRepository, times(2)).findEmployeeById(any()),
        () -> assertFalse(employeeHierarchyGraph.hasEdge(e1, e3)),
        () -> verifyNoMoreInteractions(employeeRepository));
  }
}
