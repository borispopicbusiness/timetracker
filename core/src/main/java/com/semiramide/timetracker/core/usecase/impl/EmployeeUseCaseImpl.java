package com.semiramide.timetracker.core.usecase.impl;

import com.semiramide.timetracker.core.entity.Employee;
import com.semiramide.timetracker.core.entity.EmployeeHierarchyEntry;
import com.semiramide.timetracker.core.exception.*;
import com.semiramide.timetracker.core.service.*;
import com.semiramide.timetracker.core.service.*;
import com.semiramide.timetracker.core.usecase.EmployeeUseCase;
import com.semiramide.timetracker.core.exception.*;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.eval.NotImplementedException;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Builder
@Validated
// TODO: Check is it possible to use @Valid
public class EmployeeUseCaseImpl implements EmployeeUseCase {
    private LeaveService leaveService;
    private WorklogService worklogService;
    private ProjectEmployeeService projectEmployeeService;
    private EmployeeService employeeService;
    private EmployeeHierarchyService employeeHierarchyService;

    @Override
    public List<Employee> findAllEmployeesByIds(List<UUID> employeeIds) {
        return employeeService.findAllEmployeesByIds(employeeIds);
    }

    @Override
    public List<Employee> findAllEmployees() {
        return employeeService.findAllEmployees();
    }

    @Override
    @Transactional
    public Employee createEmployee(Employee employee) throws EmailAlreadyExistsException {
        Optional<Employee> existingEmployeeOptional =
                employeeService.findEmployeeByEmail(employee.getEmail());
        if ( existingEmployeeOptional.isPresent() ) {
            log.warn(
                    "Failed creating employee with email " + employee.getEmail() + ". Email already taken!");
            throw new EmailAlreadyExistsException(
                    "Employee with email " + employee.getEmail() + " already exists!");
        }
        return employeeService.addEmployee(employee);
    }

    @Override
    public Employee updateEmployee(Employee employee)
            throws EmployeeNotFoundException, EmailAlreadyExistsException {
        Optional<Employee> employeeOptional = employeeService.findEmployeeById(employee.getId());
        if ( employeeOptional.isEmpty() ) {
            throw new EmployeeNotFoundException("Employee with not found for the given id");
        }
        if ( !employee.getEmail().equals(employeeOptional.get().getEmail())
                && employeeService.findEmployeeByEmail(employee.getEmail()).isPresent() ) {
            throw new EmailAlreadyExistsException("Email " + employee.getEmail() + " is already taken!");
        }
        employee.setPrincipalId(employeeOptional.get().getPrincipalId());
        return employeeService.updateEmployee(employee);
    }

    @Override
    public void changePassword(Employee employee) {
        // TODO: Check the password - if the format/length is ok
        employeeService.updateEmployee(employee);
    }

    @Override
    public void changePassword(String principalId, String newPassword) {
        employeeService.changePassword(principalId, newPassword);
    }

    @Override
    public List<Employee> listSuperiors(UUID employeeId) {
        throw new NotImplementedException("Method not implemented yet");
    }

    @Override
    public List<Employee> listSubordinates(UUID employeeId) {
        Optional<Employee> employeeOptional = employeeService.findEmployeeById(employeeId);
        if ( employeeOptional.isEmpty() ) {
            log.warn("Cannot list subordinates. Employee with ID " + employeeId + " not found!");
            throw new EmployeeNotFoundException("Employee with ID \" + employeeId + \" not found!\"");
        }
        return employeeHierarchyService.findAllSubordinates(employeeOptional.get());
    }

    @Override
    public List<Employee> listImmediateSuperiors(UUID employeeId) throws EmployeeNotFoundException {
        Optional<Employee> employeeOptional = employeeService.findEmployeeById(employeeId);
        if ( employeeOptional.isEmpty() ) {
            throw new EmployeeNotFoundException("Employee with ID " + employeeId + " not found!");
        }
        return employeeHierarchyService.findDirectSuperiors(employeeOptional.get());
    }

    @Override
    public List<Employee> listImmediateSubordinates(UUID employeeId)
            throws EmployeeNotFoundException {
        Optional<Employee> employeeOptional = employeeService.findEmployeeById(employeeId);
        if ( employeeOptional.isEmpty() ) {
            throw new EmployeeNotFoundException("Employee with ID " + employeeId + " not found!");
        }
        return employeeHierarchyService.findDirectSubordinates(employeeOptional.get());
    }

    @Override
    public List<Employee> listNotConnectedEmployees(UUID employeeId)
            throws EmployeeNotFoundException {
        Optional<Employee> employeeOptional = employeeService.findEmployeeById(employeeId);
        if ( employeeOptional.isEmpty() ) {
            throw new EmployeeNotFoundException("Employee with ID " + employeeId + " not found!");
        }
        return employeeHierarchyService.findNotConnectedEmployees(employeeOptional.get());
    }

    @Override
    @Transactional
    public void removeEmployee(UUID employeeId) throws EmployeeNotFoundException {
        Optional<Employee> employeeOptional = employeeService.findEmployeeById(employeeId);
        if ( employeeOptional.isEmpty() ) {
            log.warn("Employee with ID: " + employeeId + " not found");
            throw new EmployeeNotFoundException("Employee with ID: " + employeeId + " not found");
        }
        Employee employee = employeeOptional.get();
        List<Employee> directSubordinates = employeeHierarchyService.findDirectSubordinates(employee);
        List<Employee> directSuperiors = employeeHierarchyService.findDirectSuperiors(employee);

        leaveService.deleteByEmployeeId(employeeId);
        worklogService.deleteByEmployeeId(employeeId);
        projectEmployeeService.deleteByEmployeeId(employeeId);
        employeeHierarchyService.deleteEmployeeHierarchyEntriesForEmployee(employee);
        employeeService.deleteEmployeeById(employeeId);
        employeeHierarchyService.assignSubordinates(directSuperiors, directSubordinates);
    }

    @Override
    public Optional<Employee> findById(UUID id) {
        return employeeService.findEmployeeById(id);
    }

    @Override
    public Optional<Employee> findEmployeeByPrincipalId(String name) {
        return employeeService.findEmployeeByPrincipalId(name);
    }

    @Override
    @Transactional
    public Optional<EmployeeHierarchyEntry> assignSubordinate(UUID parentId, UUID childId)
            throws CycleDetectedException,
            EmployeeNotFoundException,
            InvalidArgumentException,
            AlreadyASubordinateException {
        Employee parent;
        Employee child;
        Optional<Employee> parentOptional = employeeService.findEmployeeById(parentId);
        if ( parentOptional.isEmpty() ) {
            log.warn("Employee (parent) with id " + parentId + "is not found!");
            throw new EmployeeNotFoundException(
                    "Employee (parent) with id " + parentId + "is not found!");
        }
        Optional<Employee> childOptional = employeeService.findEmployeeById(childId);
        if ( childOptional.isEmpty() ) {
            log.warn("Employee (child) with id " + childId + "is not found!");
            throw new EmployeeNotFoundException("Employee (child) with id " + childId + " is not found!");
        }
        parent = parentOptional.get();
        child = childOptional.get();
        if ( employeeHierarchyService.isSubordinate(parent, child) ) {
            log.warn(
                    "Employee with ID: "
                            + childOptional.get().getId()
                            + " is already a subordinate to employee with ID: "
                            + parentOptional.get().getId());
            throw new AlreadyASubordinateException(
                    "Employee with ID: "
                            + childOptional.get().getId()
                            + " is already a subordinate to employee with ID: "
                            + parentOptional.get().getId());
        }
        if ( employeeHierarchyService.isCycleCreated(parent, child) ) {
            log.warn(
                    "Cannot assign child "
                            + child.getEmail()
                            + " to parent "
                            + parent.getEmail()
                            + ". Cycle would be created.");
            throw new CycleDetectedException(
                    "Cannot assign child "
                            + child.getEmail()
                            + " to parent "
                            + parent.getEmail()
                            + ". Cycle would be created.");
        }
        return employeeHierarchyService.assignSubordinate(parent, child);
    }

    @Override
    @Transactional
    public void unAssignSubordinate(UUID parentId, UUID childId)
            throws EmployeeNotFoundException, NotASubordinateException {
        Employee parent;
        Employee child;
        Optional<Employee> parentOptional = employeeService.findEmployeeById(parentId);
        if ( parentOptional.isEmpty() ) {
            log.warn("Employee (parent) with id " + parentId + "is not found!");
            throw new EmployeeNotFoundException(
                    "Employee (parent) with id " + parentId + "is not found!");
        }
        Optional<Employee> childOptional = employeeService.findEmployeeById(childId);
        if ( childOptional.isEmpty() ) {
            log.warn("Employee (child) with id " + childId + "is not found!");
            throw new EmployeeNotFoundException("Employee (child) with id " + childId + " is not found!");
        }
        parent = parentOptional.get();
        child = childOptional.get();
        if ( !employeeHierarchyService.isSubordinate(parent, child) ) {
            log.warn(
                    "Employee with ID: "
                            + childId
                            + " is not a subordinate of employee with ID: "
                            + parentId);
            throw new NotASubordinateException(
                    "Employee with ID: "
                            + childId
                            + " is already a subordinate to employee with ID: "
                            + parentId);
        }
        employeeHierarchyService.deleteEmployeeHierarchyEntry(parent, child);
    }
}
