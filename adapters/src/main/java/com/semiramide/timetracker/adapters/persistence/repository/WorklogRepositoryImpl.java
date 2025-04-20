package com.semiramide.timetracker.adapters.persistence.repository;

import com.semiramide.timetracker.adapters.persistence.dto.WorklogDtoDB;
import com.semiramide.timetracker.adapters.persistence.mapper.WorklogMapperDB;
import com.semiramide.timetracker.adapters.persistence.repository.jpa.WorklogRepositoryJpa;
import com.semiramide.timetracker.adapters.persistence.repository.jpa.filter.WorklogDtoDBSearchSpecification;
import com.semiramide.timetracker.adapters.persistence.repository.pagingAndSorting.PagingAndSortingRepository;
import com.semiramide.timetracker.core.entity.Worklog;
import com.semiramide.timetracker.core.repository.WorklogRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.Builder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

@Builder
public class WorklogRepositoryImpl implements WorklogRepository {

    private final WorklogRepositoryJpa worklogRepositoryJpa;

    private final PagingAndSortingRepository pagingAndSortingRepository;

    @Override
    public Worklog saveWorklog(Worklog worklog) {
        WorklogDtoDB worklogDtoDB =
                worklogRepositoryJpa.save(WorklogMapperDB.INSTANCE.worklogToWorklogDtoDB(worklog));
        return WorklogMapperDB.INSTANCE.worklogDtoDBToWorklog(worklogDtoDB);
    }

    @Override
    public List<Worklog> findAllWorklogs() {
        return WorklogMapperDB.INSTANCE.worklogDtoDBListToWorklogList(worklogRepositoryJpa.findAll());
    }

    @Override
    public List<Worklog> findWorklogsByEmployeeId(UUID employeeId) {
        List<WorklogDtoDB> worklogDtoDBList = worklogRepositoryJpa.findByEmployeeId(employeeId);
        return WorklogMapperDB.INSTANCE.worklogDtoDBListToWorklogList(worklogDtoDBList);
    }

    @Override
    public List<Worklog> findWorklogsByEmployeeIdAndCreationDate(
            UUID employeeId, LocalDate creationDate, int page) {
        List<WorklogDtoDB> worklogDtoDBList =
                pagingAndSortingRepository.findByEmployeeIdAndCreationDate(
                        employeeId, creationDate, PageRequest.of(page, 5));
        return WorklogMapperDB.INSTANCE.worklogDtoDBListToWorklogList(worklogDtoDBList);
    }

    @Override
    public Optional<Worklog> findWorklogById(UUID id) {
        return Optional.ofNullable(
                WorklogMapperDB.INSTANCE.worklogDtoDBToWorklog(
                        worklogRepositoryJpa.findById(id).orElse(null)));
    }

    @Override
    public List<Worklog> findByAnyOf(Map<String, String[]> criteria) {
        List<Specification<WorklogDtoDB>> fields =
                criteria.entrySet().stream()
                        .map(e -> WorklogDtoDBSearchSpecification.getSpecification(e.getKey(), e.getValue()))
                        .collect(Collectors.toList());
        return WorklogMapperDB.INSTANCE.worklogDtoDBListToWorklogList(
                worklogRepositoryJpa.findAll(Specification.anyOf(fields)));
    }

    @Override
    public List<Worklog> findByAllOf(Map<String, String[]> criteria) {
        List<Specification<WorklogDtoDB>> fields =
                criteria.entrySet().stream()
                        .map(e -> WorklogDtoDBSearchSpecification.getSpecification(e.getKey(), e.getValue()))
                        .collect(Collectors.toList());
        return WorklogMapperDB.INSTANCE.worklogDtoDBListToWorklogList(
                worklogRepositoryJpa.findAll(Specification.allOf(fields)));
    }

    @Override
    public void deleteWorklogById(Worklog worklog) {
        worklogRepositoryJpa.delete(WorklogMapperDB.INSTANCE.worklogToWorklogDtoDB(worklog));
    }

    @Override
    public void deleteByEmployeeId(UUID employeeId) {
        worklogRepositoryJpa.deleteByEmployeeId(employeeId);
    }

    @Override
    public void deleteAllWorklogs() {
        worklogRepositoryJpa.deleteAll();
    }

    @Override
    public int findNumberOfWorklogs(UUID employeeId, LocalDate creationDate) {
        List<WorklogDtoDB> worklogDtoDBList =
                worklogRepositoryJpa.findByEmployeeIdAndCreationDate(employeeId, creationDate);
        return worklogDtoDBList.size();
    }
}
