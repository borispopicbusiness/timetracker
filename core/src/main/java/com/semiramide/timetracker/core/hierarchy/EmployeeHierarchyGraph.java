package com.semiramide.timetracker.core.hierarchy;

import com.semiramide.timetracker.core.entity.Employee;
import com.google.common.graph.EndpointPair;

import java.util.List;
import java.util.Set;

public interface EmployeeHierarchyGraph {
    void clear();

    void addNode(Employee employee);

    void putEdge(Employee parent, Employee child);

    void removeEdge(Employee parent, Employee child);

    boolean hasCycle();

    boolean isChild(Employee parent, Employee child);

    boolean hasNode(Employee employee);

    boolean hasEdge(Employee parent, Employee child);

    Set<Employee> reachableNodes(Employee root);

    boolean isSameAs(EmployeeHierarchyGraph other);

    List<Employee> directChildren(Employee employee);

    List<Employee> directParents(Employee employee);

    List<Employee> allParents(Employee employee);

    void deleteNode(Employee employee);

    Set<Employee> nodes();

    Set<EndpointPair<Employee>> edges();

    void print();
}
