package com.semiramide.timetracker.adapters.persistence.mapper;

import com.semiramide.timetracker.adapters.persistence.dto.EmployeeDtoDB;
import com.semiramide.timetracker.core.entity.Employee;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface EmployeeMapperDB {

    EmployeeMapperDB INSTANCE = Mappers.getMapper(EmployeeMapperDB.class);

    List<EmployeeDtoDB> employeeListToEmployeeDtoDBList(List<Employee> employeeList);

    EmployeeDtoDB employeeToEmployeeDtoDB(Employee employee);

    List<Employee> employeeDtoDBListToEmployeeList(List<EmployeeDtoDB> employeeDtoDBList);

    Employee employeeDtoDBToEmployee(EmployeeDtoDB employeeDtoDB);
}
