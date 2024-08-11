package com.semiramide.timetracker.adapters.persistence.repository;

import com.semiramide.timetracker.adapters.persistence.dto.EmployeeDtoDB;
import com.semiramide.timetracker.adapters.persistence.mapper.EmployeeMapperDB;
import com.semiramide.timetracker.adapters.persistence.repository.jpa.EmployeeRepositoryJpa;
import com.semiramide.timetracker.core.entity.Employee;
import com.semiramide.timetracker.core.repository.EmployeeRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.Builder;

@Builder
public class EmployeeRepositoryImpl implements EmployeeRepository {

  private final EmployeeRepositoryJpa employeeRepositoryJpa;

  @Override
  public List<Employee> findAllEmployees() {
    return EmployeeMapperDB.INSTANCE.employeeDtoDBListToEmployeeList(
        employeeRepositoryJpa.findAll());
  }

  @Override
  public List<Employee> findAllEmployeesByIds(List<UUID> employeeIds) {
    return EmployeeMapperDB.INSTANCE.employeeDtoDBListToEmployeeList(
        employeeRepositoryJpa.findAllByIdIn(employeeIds));
  }

  @Override
  public Employee saveEmployee(Employee employee) {
    EmployeeDtoDB newEmployeeDtoDB = EmployeeMapperDB.INSTANCE.employeeToEmployeeDtoDB(employee);
    EmployeeDtoDB savedEmployeeDtoDB = employeeRepositoryJpa.save(newEmployeeDtoDB);
    Employee createdEmployee =
        EmployeeMapperDB.INSTANCE.employeeDtoDBToEmployee(savedEmployeeDtoDB);
    return createdEmployee;
  }

  @Override
  public Optional<Employee> findEmployeeById(UUID id) {
    return Optional.ofNullable(
        EmployeeMapperDB.INSTANCE.employeeDtoDBToEmployee(
            employeeRepositoryJpa.findById(id).orElse(null)));
  }

  @Override
  public Optional<Employee> findEmployeeByEmail(String email) {
    EmployeeDtoDB employeeDtoDB = employeeRepositoryJpa.findByEmail(email);
    Employee employee = EmployeeMapperDB.INSTANCE.employeeDtoDBToEmployee(employeeDtoDB);
    return Optional.ofNullable(employee);
  }

  @Override
  public void deleteEmployeeById(UUID id) {
    employeeRepositoryJpa.deleteById(id);
  }

  @Override
  public void deleteAllEmployees() {
    employeeRepositoryJpa.deleteAll();
  }

  @Override
  public Optional<Employee> findEmployeeByPrincipalId(String principalId) {
    return Optional.ofNullable(
        EmployeeMapperDB.INSTANCE.employeeDtoDBToEmployee(
            employeeRepositoryJpa.findByPrincipalId(principalId).orElse(null)));
  }
}
