package com.semiramide.timetracker.core.usecase.util;

import com.semiramide.timetracker.core.entity.Employee;
import com.semiramide.timetracker.core.entity.EmployeeHierarchyEntry;
import com.semiramide.timetracker.core.hierarchy.EmployeeHierarchyGraph;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Helper {

  public void initializeNodesAndAddToGraph(EmployeeHierarchyGraph graph, Employee... employees) {
    int count = 0;
    for (Employee e : employees) {
      UUID id = UUID.randomUUID();
      Employee tmp =
          Employee.builder()
              .id(id)
              .email("email" + count)
              .firstName("first" + count)
              .lastName("last" + count)
              .password("pass" + count)
              .build();
      e.setEmail(tmp.getEmail());
      e.setId(tmp.getId());
      e.setFirstName(tmp.getFirstName());
      e.setLastName(tmp.getLastName());
      e.setPassword(tmp.getPassword());
      graph.addNode(e);
    }
  }

  public List<Employee> generateEmployeesList() {
    List<Employee> employees = new ArrayList<>();
    for (int i = 0; i < 7; i++) {
      UUID id = UUID.randomUUID();
      Employee tmp =
          Employee.builder()
              .id(id)
              .email("email" + i)
              .firstName("first" + i)
              .lastName("last" + i)
              .password("pass" + i)
              .build();
      employees.add(tmp);
    }
    return employees;
  }

  public void initializeStartingGraph(EmployeeHierarchyGraph graph, List<Employee> employees) {
    for (Employee e : employees) {
      graph.addNode(e);
    }
    graph.putEdge(employees.get(0), employees.get(1));
    graph.putEdge(employees.get(0), employees.get(2));
    graph.putEdge(employees.get(1), employees.get(3));
    graph.putEdge(employees.get(1), employees.get(4));
    graph.putEdge(employees.get(2), employees.get(4));
    graph.putEdge(employees.get(2), employees.get(5));
    graph.putEdge(employees.get(4), employees.get(6));
  }

  public List<EmployeeHierarchyEntry> getEdges(List<Employee> employees) {

    EmployeeHierarchyEntry ehe1 =
        EmployeeHierarchyEntry.builder()
            .id(UUID.randomUUID())
            .parentId(employees.get(0).getId())
            .childId(employees.get(1).getId())
            .build();

    EmployeeHierarchyEntry ehe2 =
        EmployeeHierarchyEntry.builder()
            .id(UUID.randomUUID())
            .parentId(employees.get(1).getId())
            .childId(employees.get(3).getId())
            .build();

    EmployeeHierarchyEntry ehe3 =
        EmployeeHierarchyEntry.builder()
            .id(UUID.randomUUID())
            .parentId(employees.get(1).getId())
            .childId(employees.get(4).getId())
            .build();

    return List.of(ehe1, ehe2, ehe3);
  }

  public void initializeExpectedGraph(EmployeeHierarchyGraph graph, List<Employee> employees) {
    initializeStartingGraph(graph, employees);
    graph.deleteNode(employees.get(1));
    graph.putEdge(employees.get(0), employees.get(3));
  }
}
