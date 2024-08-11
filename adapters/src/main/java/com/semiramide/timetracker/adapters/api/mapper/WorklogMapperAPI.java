package com.semiramide.timetracker.adapters.api.mapper;

import com.semiramide.timetracker.adapters.api.dto.WorklogDtoAPI;
import com.semiramide.timetracker.core.entity.Worklog;
import com.semiramide.timetracker.core.entity.enums.WorklogType;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface WorklogMapperAPI {

  WorklogMapperAPI INSTANCE = Mappers.getMapper(WorklogMapperAPI.class);

  @Named(value = "worklogTypeAsStringToWorklogType")
  static WorklogType worklogTypeAsStringToWorklogType(String wlType) {
    return WorklogType.fromName(wlType);
  }

  @Named(value = "worklogTypeToWorklogTypeAsString")
  static String worklogTypeToWorklogTypeAsString(WorklogType worklogType) {
    return worklogType.getName();
  }

  @Mapping(source = "type", target = "type", qualifiedByName = "worklogTypeAsStringToWorklogType")
  Worklog worklogDtoAPIToWorklog(WorklogDtoAPI worklogDtoAPI);

  @Mapping(source = "type", target = "type", qualifiedByName = "worklogTypeToWorklogTypeAsString")
  WorklogDtoAPI worklogToWorklogDtoAPI(Worklog worklog);

  List<Worklog> worklogDtoAPIListToWorklogList(List<WorklogDtoAPI> worklogDtoAPIList);

  List<WorklogDtoAPI> worklogListToWorklogDtoAPIList(List<Worklog> worklogList);
}
