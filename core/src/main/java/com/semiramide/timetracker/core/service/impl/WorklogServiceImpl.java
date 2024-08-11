package com.semiramide.timetracker.core.service.impl;

import com.semiramide.timetracker.core.entity.Employee;
import com.semiramide.timetracker.core.entity.Project;
import com.semiramide.timetracker.core.entity.Worklog;
import com.semiramide.timetracker.core.entity.enums.WorklogType;
import com.semiramide.timetracker.core.exception.InvalidArgumentException;
import com.semiramide.timetracker.core.repository.WorklogRepository;
import com.semiramide.timetracker.core.service.WorklogService;
import com.semiramide.timetracker.core.util.WorklogExportDTO;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.LocalDate;
import java.util.*;

@Builder
@Slf4j
public class WorklogServiceImpl implements WorklogService {

    private final WorklogRepository worklogRepository;

    @Override
    public List<WorklogExportDTO> prepareWorklogsForExport(List<Worklog> worklogs, List<Employee> employees, List<Project> projects) {
        List<WorklogExportDTO> preparedWorklogs = new ArrayList<>();
        for (Worklog worklog : worklogs) {
            UUID employeeId = worklog.getEmployeeId();
            UUID projectId = worklog.getProjectId();
            WorklogExportDTO exportDTO = WorklogExportDTO.builder()
                    .task(worklog.getTaskName())
                    .description(worklog.getDescription())
                    .type(worklog.getType().getName())
                    .date(worklog.getCreationDate())
                    .time(worklog.getTotalTime().doubleValue())
                    .employee(getEmployeeNameById(employeeId, employees))
                    .project(getProjectNameById(projectId, projects))
                    .build();
            preparedWorklogs.add(exportDTO);
        }
        return preparedWorklogs;
    }

    private String getProjectNameById(UUID projectId, List<Project> projects) {
        return projects.stream()
                .filter(project -> projectId.equals(project.getId()))
                .map(Project::getName)
                .findFirst()
                .orElse("");
    }

    private String getEmployeeNameById(UUID employeeId, List<Employee> employees) {
        return employees.stream()
                .filter(employee -> employeeId.equals(employee.getId()))
                .map(employee -> employee.getFirstName() + " " + employee.getLastName())
                .findFirst()
                .orElse("");
    }

    @Override
    public List<Worklog> findAllWorklogs() {
        return worklogRepository.findAllWorklogs();
    }

    @Override
    public Worklog addWorklog(Worklog worklog) throws InvalidArgumentException {
        validateAndAdjustWorklogTimes(worklog);
        return worklogRepository.saveWorklog(worklog);
    }

    @Override
    public void updateWorklog(Worklog worklog) throws InvalidArgumentException {
//        preserveWorklogCreationDate(worklog);
        validateAndAdjustWorklogTimes(worklog);
        worklogRepository.saveWorklog(worklog);
    }

    @Override
    public Optional<Worklog> findWorklogById(UUID id) {
        return worklogRepository.findWorklogById(id);
    }

    @Override
    public List<Worklog> findWorklogByEmployeeId(UUID employeeId) {
        return worklogRepository.findWorklogsByEmployeeId(employeeId);
    }

    @Override
    public List<Worklog> findWorklogByEmployeeIdAndCreationDate(UUID employeeId, LocalDate creationDate, int page) {
        return worklogRepository.findWorklogsByEmployeeIdAndCreationDate(employeeId, creationDate, page);
    }

    @Override
    public void deleteWorklogById(UUID id) {
        Optional<Worklog> worklog = worklogRepository.findWorklogById(id);
        if (worklog.isPresent()) {
            worklogRepository.deleteWorklogById(worklog.get());
        } else {
            log.error("There is no worklog with id: " + id);
        }
    }

    @Override
    public void deleteByEmployeeId(UUID employeeId) {
        worklogRepository.deleteByEmployeeId(employeeId);
    }

    @Override
    public void deleteAllWorklogs() {
        worklogRepository.deleteAllWorklogs();
    }

    @Override
    public List<Worklog> listWorklogByAnyCriteria(Map<String, String[]> criteria) {
        return worklogRepository.findByAnyOf(criteria);
    }

    @Override
    public List<Worklog> listWorklogByAllCriteria(Map<String, String[]> criteria) {
        return worklogRepository.findByAllOf(criteria);
    }

    @Override
    public boolean isWorklogsDateInTheFuture(Worklog worklog) {
        return worklog.getCreationDate().isAfter(LocalDate.now());
    }

    @Override
    public boolean isWorklogsDateLocked(Worklog worklog) {
        int daysToSubtract = 3;
        // Take weekend into account
        if (LocalDate.now().getDayOfWeek().getValue() <= 3) {
            daysToSubtract = 5;
        }
        return worklog.getCreationDate().isBefore(LocalDate.now().minusDays(daysToSubtract));
    }

    private void validateAndAdjustWorklogTimes(Worklog worklog) throws InvalidArgumentException {
        if ((worklog.getStartTime() == null || worklog.getEndTime() == null)
                && worklog.getTotalTime() == null) {
            log.warn("Invalid data. Please, enter correct data about start, end and total time!");
            throw new InvalidArgumentException("Invalid data. Enter either start time and end time OR total time.");
        }
        if (worklog.getStartTime() != null && worklog.getEndTime() != null) {
            if (worklog.getStartTime().isAfter(worklog.getEndTime())) {
                log.warn("Invalid data. Please, enter correct data about start, end and total time!");
                throw new InvalidArgumentException("Invalid data. Start time cannot be after end time.");
            }
            worklog.setTotalTime(Double.valueOf(Duration.between(worklog.getStartTime(), worklog.getEndTime()).toSeconds()));
        }
        worklog.setTotalTime(((double) Math.round(worklog.getTotalTime() * 100) / 100));
        if (worklog.getCreationDate() == null) {
            worklog.setCreationDate(LocalDate.now());
        }
    }

    private void preserveWorklogCreationDate(Worklog worklog) {
        Optional<Worklog> existingWorklogOptional = findWorklogById(worklog.getId());
        if (existingWorklogOptional.isPresent()) {
            worklog.setCreationDate(existingWorklogOptional.get().getCreationDate());
        }
    }

    @Override
    public List<WorklogType> listWorklogTypes() {
        return List.of(WorklogType.values());
    }

    @Override
    public int findNumberOfWorklogs(UUID employeeId, LocalDate creationDate) {
        return worklogRepository.findNumberOfWorklogs(employeeId, creationDate);
    }
}
