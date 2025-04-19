package com.semiramide.timetracker.core.repository;

import com.semiramide.timetracker.core.entity.Employee;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmployeeRepository {
    List<Employee> findAllEmployees();

    List<Employee> findAllEmployeesByIds(List<UUID> employeeIds);

    Optional<Employee> findEmployeeByEmail(String email);

    Employee saveEmployee(Employee employee);

    Optional<Employee> findEmployeeById(UUID id);

    void deleteEmployeeById(UUID id);

    void deleteAllEmployees();

    Optional<Employee> findEmployeeByPrincipalId(String principalId);
}
