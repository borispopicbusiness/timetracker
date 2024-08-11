package com.semiramide.timetracker.core.repository;

import com.semiramide.timetracker.core.entity.Worklog;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface WorklogRepository {

  Worklog saveWorklog(Worklog worklog);

  List<Worklog> findAllWorklogs();

  List<Worklog> findWorklogsByEmployeeId(UUID employeeId);

  List<Worklog> findWorklogsByEmployeeIdAndCreationDate(
      UUID employeeId, LocalDate creationDate, int page);

  Optional<Worklog> findWorklogById(UUID id);

  List<Worklog> findByAnyOf(Map<String, String[]> criteria);

  List<Worklog> findByAllOf(Map<String, String[]> criteria);

  void deleteWorklogById(Worklog worklog);

  void deleteByEmployeeId(UUID employeeId);

  void deleteAllWorklogs();

  int findNumberOfWorklogs(UUID employeeId, LocalDate creationDate);
}
