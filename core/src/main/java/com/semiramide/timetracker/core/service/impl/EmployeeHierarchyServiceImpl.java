package com.semiramide.timetracker.core.service.impl;

import com.semiramide.timetracker.core.entity.Employee;
import com.semiramide.timetracker.core.entity.EmployeeHierarchyEntry;
import com.semiramide.timetracker.core.event.AppEventPublisher;
import com.semiramide.timetracker.core.event.event.AllEmployeeHierarchyEntriesDeletedForEmployeeEvent;
import com.semiramide.timetracker.core.event.event.SubordinateAssignedEvent;
import com.semiramide.timetracker.core.event.event.SubordinateUnassignedEvent;
import com.semiramide.timetracker.core.hierarchy.EmployeeHierarchyGraph;
import com.semiramide.timetracker.core.repository.EmployeeHierarchyRepository;
import com.semiramide.timetracker.core.service.EmployeeHierarchyService;
import com.semiramide.timetracker.core.service.EmployeeService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Builder
public class EmployeeHierarchyServiceImpl implements EmployeeHierarchyService {
    private EmployeeService employeeService;
    private EmployeeHierarchyRepository employeeHierarchyRepository;
    private EmployeeHierarchyGraph employeeHierarchyGraph;
    private AppEventPublisher eventPublisher;

    @Override
    public synchronized void loadGraph() {
        // TODO: Should we stop application if the employees are not found
        employeeHierarchyGraph.clear();
        employeeService.findAllEmployees().forEach(e -> employeeHierarchyGraph.addNode(e));
        for ( EmployeeHierarchyEntry entry :
                employeeHierarchyRepository.findAllEmployeeHierarchyEntries() ) {
            Optional<Employee> parent = employeeService.findEmployeeById(entry.getParentId());
            if ( parent.isEmpty() ) {
                log.error("Parent with id: " + entry.getParentId() + " not found.");
                continue;
            }
            Optional<Employee> child = employeeService.findEmployeeById(entry.getChildId());
            if ( child.isEmpty() ) {
                log.error("Child with id: " + entry.getChildId() + " not found.");
                continue;
            }
            employeeHierarchyGraph.putEdge(parent.get(), child.get());
        }
    }

    @Override
    public synchronized void printGraph() {
        employeeHierarchyGraph.print();
    }

    @Override
    public synchronized Optional<EmployeeHierarchyEntry> assignSubordinate(
            Employee parent, Employee child) {
        EmployeeHierarchyEntry employeeHierarchyEntry =
                EmployeeHierarchyEntry.builder().parentId(parent.getId()).childId(child.getId()).build();
        Optional<EmployeeHierarchyEntry> employeeHierarchyEntryOptional =
                employeeHierarchyRepository.saveEmployeeHierarchyEntry(employeeHierarchyEntry);
        eventPublisher.publishEvent(
                SubordinateAssignedEvent.builder().parent(parent).child(child).build());
        return employeeHierarchyEntryOptional;
    }

    @Override
    public synchronized void assignSubordinates(
            List<Employee> directSuperiors, List<Employee> directSubordinates) {
        for ( Employee parent : directSuperiors ) {
            for ( Employee child : directSubordinates ) {
                if ( !employeeHierarchyGraph.reachableNodes(parent).contains(child) ) {
                    assignSubordinate(parent, child);
                }
            }
        }
    }

    @Override
    public synchronized boolean isSubordinate(Employee parent, Employee child) {
        return employeeHierarchyGraph.reachableNodes(parent).contains(child);
    }

    @Override
    public synchronized List<Employee> findNotConnectedEmployees(Employee employee) {
        List<Employee> excludedEmployees = new ArrayList<>(findAllSubordinates(employee));
        excludedEmployees.addAll(findAllSuperiors(employee));
        excludedEmployees.add(employee);
        List<Employee> el = new ArrayList<>(employeeHierarchyGraph.nodes());
        el.removeAll(excludedEmployees);
        return el;
    }

    @Override
    public synchronized List<Employee> findAllSubordinates(Employee employee) {
        return employeeHierarchyGraph.reachableNodes(employee).stream()
                .filter(parent -> !parent.equals(employee))
                .toList();
    }

    @Override
    public synchronized boolean isCycleCreated(Employee parent, Employee child) {
        employeeHierarchyGraph.putEdge(parent, child);
        boolean cycleCreated = employeeHierarchyGraph.hasCycle();
        employeeHierarchyGraph.removeEdge(parent, child);
        return cycleCreated;
    }

    @Override
    public synchronized List<Employee> findDirectSubordinates(Employee employee) {
        return employeeHierarchyGraph.directChildren(employee);
    }

    @Override
    public synchronized List<Employee> findDirectSuperiors(Employee employee) {
        return employeeHierarchyGraph.directParents(employee);
    }

    @Override
    public synchronized List<Employee> findAllSuperiors(Employee employee) {
        return employeeHierarchyGraph.allParents(employee);
    }

    @Override
    public synchronized void deleteEmployeeHierarchyEntriesForEmployee(Employee employee) {
        employeeHierarchyRepository.deleteEmployeeHierarchyEntriesByEmployeeId(employee.getId());
        eventPublisher.publishEvent(
                AllEmployeeHierarchyEntriesDeletedForEmployeeEvent.builder()
                        .employeeId(employee.getId())
                        .build());
    }

    @Override
    public synchronized void deleteEmployeeHierarchyEntry(EmployeeHierarchyEntry entry) {
        employeeHierarchyRepository.deleteEmployeeHierarchyEntry(entry);
        eventPublisher.publishEvent(SubordinateUnassignedEvent.builder().entry(entry).build());
    }

    @Override
    public synchronized void deleteEmployeeHierarchyEntry(Employee parent, Employee child) {
        employeeHierarchyRepository.deleteByParentIdAndChildId(parent.getId(), child.getId());
        eventPublisher.publishEvent(
                SubordinateUnassignedEvent.builder()
                        .entry(
                                EmployeeHierarchyEntry.builder()
                                        .parentId(parent.getId())
                                        .childId(child.getId())
                                        .build())
                        .build());
    }
}
