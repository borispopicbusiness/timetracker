package com.semiramide.timetracker.adapters.event;

import com.semiramide.timetracker.core.event.AppEventListener;
import com.semiramide.timetracker.core.event.event.*;
import com.semiramide.timetracker.core.service.EmployeeHierarchyService;
import lombok.Builder;
import org.springframework.context.event.EventListener;

@Builder
public class AppEventListenerImpl implements AppEventListener {
    private final EmployeeHierarchyService employeeHierarchyService;

    @Override
    @EventListener(classes = {EmployeeCreatedEvent.class})
    public void employeeCreatedEvent(EmployeeCreatedEvent event) {
        employeeHierarchyService.loadGraph();
    }

    @Override
    @EventListener(classes = {EmployeeUpdatedEvent.class})
    public void employeeUpdatedEvent(EmployeeUpdatedEvent event) {
        employeeHierarchyService.loadGraph();
    }

    @Override
    @EventListener(classes = {EmployeeDeletedEvent.class})
    public void employeeDeletedEvent(EmployeeDeletedEvent event) {
        employeeHierarchyService.loadGraph();
    }

    @Override
    @EventListener(classes = {SubordinateAssignedEvent.class})
    public void subordinateAssignedEvent(SubordinateAssignedEvent event) {
        employeeHierarchyService.loadGraph();
    }

    @Override
    @EventListener(classes = {SubordinateUnassignedEvent.class})
    public void subordinateUnassignedEvent(SubordinateUnassignedEvent event) {
        employeeHierarchyService.loadGraph();
    }

    @Override
    @EventListener(classes = {AllEmployeeHierarchyEntriesDeletedForEmployeeEvent.class})
    public void allEmployeeHierarchyEntriesDeletedForEmployeeEvent(
            AllEmployeeHierarchyEntriesDeletedForEmployeeEvent event) {
        employeeHierarchyService.loadGraph();
    }
}
