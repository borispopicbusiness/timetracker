package com.semiramide.timetracker.adapters.persistence.mapper;

import com.semiramide.timetracker.adapters.persistence.dto.ProjectEmployeeDtoDB;
import com.semiramide.timetracker.core.entity.ProjectEmployee;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProjectEmployeeMapperDB {
    ProjectEmployeeMapperDB INSTANCE = Mappers.getMapper(ProjectEmployeeMapperDB.class);

    List<ProjectEmployeeDtoDB> projectEmployeeListToProjectEmployeeDtoDBList(
            List<ProjectEmployeeDtoDB> projectEmployeeList);

    ProjectEmployeeDtoDB projectEmployeeToProjectEmployeeDtoDB(ProjectEmployee projectEmployee);

    List<ProjectEmployee> projectEmployeeDtoDBListToProjectEmployeeList(
            List<ProjectEmployeeDtoDB> projectEmployeeDtoDBList);

    ProjectEmployee projectEmployeeDtoDBToProjectEmployee(ProjectEmployeeDtoDB projectEmployeeDtoDB);
}
