package com.semiramide.timetracker.adapters.persistence.mapper;

import com.semiramide.timetracker.adapters.persistence.dto.ProjectDtoDB;
import com.semiramide.timetracker.core.entity.Project;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProjectMapperDB {

    ProjectMapperDB INSTANCE = Mappers.getMapper(ProjectMapperDB.class);

    List<ProjectDtoDB> projectListToProjectDtoDBList(List<Project> projectList);

    ProjectDtoDB projectToProjectDtoDB(Project project);

    List<Project> projectDtoDBListToProjectList(List<ProjectDtoDB> projectDtoDBList);

    Project projectDtoDBToProject(ProjectDtoDB projectDtoDB);
}
