package com.semiramide.timetracker.core.service.impl;

import com.semiramide.timetracker.core.entity.Employee;
import com.semiramide.timetracker.core.event.AppEventPublisher;
import com.semiramide.timetracker.core.event.event.EmployeeCreatedEvent;
import com.semiramide.timetracker.core.event.event.EmployeeDeletedEvent;
import com.semiramide.timetracker.core.event.event.EmployeeUpdatedEvent;
import com.semiramide.timetracker.core.exception.EmployeeNotFoundException;
import com.semiramide.timetracker.core.hierarchy.EmployeeHierarchyGraph;
import com.semiramide.timetracker.core.repository.EmployeeRepository;
import com.semiramide.timetracker.core.security.Role;
import com.semiramide.timetracker.core.security.SecurityProvider;
import com.semiramide.timetracker.core.service.EmployeeService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Builder
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeHierarchyGraph employeeHierarchyGraph;
    private final AppEventPublisher eventPublisher;
    private final SecurityProvider securityProvider;

    @Override
    public List<Employee> findAllEmployees() {
        return employeeRepository.findAllEmployees();
    }

    @Override
    public List<Employee> findAllEmployeesByIds(List<UUID> employeeIds) {
        return employeeRepository.findAllEmployeesByIds(employeeIds);
    }

    @Override
    public Optional<Employee> findEmployeeByEmail(String email) {
        return employeeRepository.findEmployeeByEmail(email);
    }

    @Override
    public Employee addEmployee(Employee employee) {
        securityProvider.authorize();
        String principalId = securityProvider.createPrincipal(employee);
        securityProvider.assignEmployeeRoles(principalId, Role.EMPLOYEE);
        employee.setPrincipalId(principalId);
        employee.setFreeDaysLeft(25);
        Employee createdEmployeeOptional = employeeRepository.saveEmployee(employee);
        eventPublisher.publishEvent(
                EmployeeCreatedEvent.builder().employee(createdEmployeeOptional).build());
        return createdEmployeeOptional;
    }

    @Override
    public Employee updateEmployee(Employee employee) {
        securityProvider.updatePrincipal(employee);
        Employee e = employeeRepository.saveEmployee(employee);
        eventPublisher.publishEvent(EmployeeUpdatedEvent.builder().employee(e).build());
        return e;
    }

    @Override
    public void changePassword(String principalId, String newPassword) {
        securityProvider.changePassword(principalId, newPassword);
    }

    @Override
    public Optional<Employee> findEmployeeById(UUID id) {
        return employeeRepository.findEmployeeById(id);
    }

    @Override
    public void deleteEmployeeById(UUID id) throws EmployeeNotFoundException {
        Employee employee =
                employeeRepository
                        .findEmployeeById(id)
                        .orElseThrow(
                                () -> new EmployeeNotFoundException("Employee with id " + id + " not found!"));
        securityProvider.deletePrincipal(employee.getPrincipalId());
        employeeRepository.deleteEmployeeById(id);
        eventPublisher.publishEvent(EmployeeDeletedEvent.builder().employee(employee).build());
    }

    @Override
    public void deleteAllEmployees() {
        employeeRepository.deleteAllEmployees();
    }

    @Override
    public Optional<Employee> findEmployeeByPrincipalId(String name) {
        return employeeRepository.findEmployeeByPrincipalId(name);
    }

    @Override
    public Employee calculateFreeDaysLeft(Integer daysTaken, Employee employee) {
        employee.setFreeDaysLeft(employee.getFreeDaysLeft() - daysTaken);
        Employee e = employeeRepository.saveEmployee(employee);
        eventPublisher.publishEvent(EmployeeUpdatedEvent.builder().employee(employee).build());
        return e;
    }
}
