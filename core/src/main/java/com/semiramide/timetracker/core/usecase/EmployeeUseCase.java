package com.semiramide.timetracker.core.usecase;

import com.semiramide.timetracker.core.entity.Employee;
import com.semiramide.timetracker.core.entity.EmployeeHierarchyEntry;
import com.semiramide.timetracker.core.exception.*;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmployeeUseCase {
    List<Employee> findAllEmployeesByIds(List<UUID> employeeIds);

    List<Employee> findAllEmployees();

    Employee createEmployee(Employee employee) throws EmailAlreadyExistsException;

    Employee updateEmployee(Employee employee)
            throws EmployeeNotFoundException, EmailAlreadyExistsException;

    void changePassword(Employee employee);

    void changePassword(String principalId, String newPassword);

    Optional<EmployeeHierarchyEntry> assignSubordinate(
            @NotNull(message = "Parent ID must not be null") UUID parentId,
            @NotNull(message = "Child ID must not be null") UUID childId)
            throws CycleDetectedException,
            EmployeeNotFoundException,
            InvalidArgumentException,
            AlreadyASubordinateException;

    void unAssignSubordinate(UUID parentId, UUID childId)
            throws EmployeeNotFoundException, NotASubordinateException;

    List<Employee> listSuperiors(UUID employeeId);

    List<Employee> listSubordinates(UUID employeeId);

    List<Employee> listImmediateSuperiors(UUID employeeId) throws EmployeeNotFoundException;

    List<Employee> listImmediateSubordinates(UUID employeeId) throws EmployeeNotFoundException;

    List<Employee> listNotConnectedEmployees(UUID employeeId) throws EmployeeNotFoundException;

    void removeEmployee(UUID employeeId) throws EmployeeNotFoundException;

    Optional<Employee> findById(UUID id);

    Optional<Employee> findEmployeeByPrincipalId(String name);
}
