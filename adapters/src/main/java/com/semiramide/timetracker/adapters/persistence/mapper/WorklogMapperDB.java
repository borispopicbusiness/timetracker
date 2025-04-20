package com.semiramide.timetracker.adapters.persistence.mapper;

import com.semiramide.timetracker.adapters.persistence.dto.WorklogDtoDB;
import com.semiramide.timetracker.core.entity.Worklog;
import com.semiramide.timetracker.core.entity.enums.WorklogType;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface WorklogMapperDB {

    WorklogMapperDB INSTANCE = Mappers.getMapper(WorklogMapperDB.class);

    @Named(value = "worklogTypeAsStringToWorklogType")
    static WorklogType worklogTypeAsStringToWorklogType(String wlType) {
        return WorklogType.fromName(wlType);
    }

    @Named(value = "worklogTypeToWorklogTypeAsString")
    static String worklogTypeToWorklogTypeAsString(WorklogType worklogType) {
        return worklogType.getName();
    }

    @Mapping(source = "type", target = "type", qualifiedByName = "worklogTypeAsStringToWorklogType")
    Worklog worklogDtoDBToWorklog(WorklogDtoDB worklogDtoDB);

    @Mapping(source = "type", target = "type", qualifiedByName = "worklogTypeToWorklogTypeAsString")
    WorklogDtoDB worklogToWorklogDtoDB(Worklog worklog);

    List<Worklog> worklogDtoDBListToWorklogList(List<WorklogDtoDB> worklogDtoDBList);

    List<WorklogDtoDB> worklogListToWorklogDtoDBList(List<Worklog> worklogList);
}
