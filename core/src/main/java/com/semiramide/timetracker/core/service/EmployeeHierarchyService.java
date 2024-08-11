package com.semiramide.timetracker.core.service;

import com.semiramide.timetracker.core.entity.Employee;
import com.semiramide.timetracker.core.entity.EmployeeHierarchyEntry;
import java.util.List;
import java.util.Optional;

public interface EmployeeHierarchyService {

  void loadGraph();

  void printGraph();

  Optional<EmployeeHierarchyEntry> assignSubordinate(Employee parent, Employee child);

  List<Employee> findNotConnectedEmployees(Employee employee);

  List<Employee> findAllSubordinates(Employee employee);

  List<Employee> findAllSuperiors(Employee employee);

  List<Employee> findDirectSubordinates(Employee employee);

  List<Employee> findDirectSuperiors(Employee employee);

  boolean isCycleCreated(Employee parent, Employee child);

  boolean isSubordinate(Employee parent, Employee child);

  void deleteEmployeeHierarchyEntriesForEmployee(Employee employee);

  void deleteEmployeeHierarchyEntry(EmployeeHierarchyEntry entry);

  void deleteEmployeeHierarchyEntry(Employee parent, Employee child);

  void assignSubordinates(List<Employee> directSuperiors, List<Employee> directSubordinates);
}
