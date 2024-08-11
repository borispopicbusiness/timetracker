package com.semiramide.timetracker.core.usecase.impl;

import com.semiramide.timetracker.core.entity.Employee;
import com.semiramide.timetracker.core.entity.Project;
import com.semiramide.timetracker.core.entity.Worklog;
import com.semiramide.timetracker.core.entity.enums.WorklogType;
import com.semiramide.timetracker.core.exception.*;
import com.semiramide.timetracker.core.service.EmployeeHierarchyService;
import com.semiramide.timetracker.core.service.EmployeeService;
import com.semiramide.timetracker.core.service.ProjectService;
import com.semiramide.timetracker.core.service.WorklogService;
import com.semiramide.timetracker.core.usecase.WorklogUseCase;
import com.semiramide.timetracker.core.util.WorklogExportDTO;
import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Builder
@Slf4j
public class WorklogUseCaseImpl implements WorklogUseCase {

    private WorklogService worklogService;
    private ProjectService projectService;
    private EmployeeService employeeService;
    private EmployeeHierarchyService employeeHierarchyService;

    @Override
    public List<WorklogExportDTO> prepareWorklogsForExport(List<Worklog> worklogs,
                                                           List<Employee> employees, List<Project> projects) {
        return worklogService.prepareWorklogsForExport(worklogs, employees, projects);
    }

    @Override
    @Transactional
    public Worklog addWorklog(Worklog worklog) throws EmployeeNotFoundException, NoProjectFoundException, InvalidArgumentException {
        Optional<Employee> employee = employeeService.findEmployeeById(worklog.getEmployeeId());
        if (employee.isEmpty()) {
            log.warn("Employee with id: " + worklog.getEmployeeId() + " not found.");
            throw new EmployeeNotFoundException("There is no employee with id: " + worklog.getEmployeeId());
        }
        Optional<Project> project = projectService.findProjectById(worklog.getProjectId());
        if (project.isEmpty()) {
            log.warn("Project with id: " + worklog.getProjectId() + " not found.");
            throw new NoProjectFoundException("There is no project with id: " + worklog.getProjectId());
        }
        if (worklogService.isWorklogsDateLocked(worklog)) {
            throw new WorklogDateLockedException("You cannot add worklogs for more than 3 days in the past!");
        }
        if (worklogService.isWorklogsDateInTheFuture(worklog)) {
            throw new WorklogDateInFutureException("You cannot add worklogs in the future (unless you are Jesus Christ)!");
        }
        return worklogService.addWorklog(worklog);
    }

    @Override
    public Worklog addWorklog(UUID accessingEmployeeId, Worklog worklog) throws EmployeeNotFoundException, NoProjectFoundException, InvalidArgumentException {
        Optional<Employee> accessingEmployeeOptional = employeeService.findEmployeeById(accessingEmployeeId);
        if (accessingEmployeeOptional.isEmpty()) {
            log.warn("Employee not found for ID: " + accessingEmployeeId);
            throw new EmployeeNotFoundException("Employee not found for the given ID");
        }
        Optional<Employee> employeeOptional = employeeService.findEmployeeById(worklog.getEmployeeId());
        if (employeeOptional.isEmpty()) {
            log.warn("Employee with id: " + worklog.getEmployeeId() + " not found.");
            throw new EmployeeNotFoundException("There is no employee with id: " + worklog.getEmployeeId());
        }
        Optional<Project> project = projectService.findProjectById(worklog.getProjectId());
        if (project.isEmpty()) {
            log.warn("Project with id: " + worklog.getProjectId() + " not found.");
            throw new NoProjectFoundException("There is no project with id: " + worklog.getProjectId());
        }
        if (worklogService.isWorklogsDateInTheFuture(worklog)) {
            throw new WorklogDateInFutureException("You cannot add worklogs in the future (unless you are Jesus Christ)!");
        }
        if (worklog.getEmployeeId().equals(accessingEmployeeId)) {
            if (worklogService.isWorklogsDateLocked(worklog)) {
                throw new WorklogDateLockedException("You cannot add worklogs for more than 3 days in the past!");
            }
            return worklogService.addWorklog(worklog);
        } else {
            Employee accessingEmployee = accessingEmployeeOptional.get();
            Employee employee = employeeOptional.get();
            if (employeeHierarchyService.isSubordinate(accessingEmployee, employee)) {
                return worklogService.addWorklog(worklog);
            } else {
                log.warn("User with ID " + accessingEmployeeId + " is not a superior to user with ID " + worklog.getEmployeeId());
                throw new NotASubordinateException("You do not have permission to add worklogs for this user!");
            }
        }
    }

    @Override
    @Transactional
    public void updateWorklog(Worklog worklog) throws WorklogNotFoundException, EmployeeNotFoundException, NoProjectFoundException, InvalidArgumentException {
        Optional<Worklog> optionalWorklog = worklogService.findWorklogById(worklog.getId());
        if (optionalWorklog.isEmpty()) {
            log.warn("Wrong worklog id!");
            throw new WorklogNotFoundException("There is no worklog with id:" + worklog.getId());
        }
        Optional<Employee> employee = employeeService.findEmployeeById(worklog.getEmployeeId());
        if (employee.isEmpty()) {
            log.warn("There is no employee with id: " + worklog.getEmployeeId());
            throw new EmployeeNotFoundException("There is no employee with id: " + worklog.getEmployeeId());
        }
        Optional<Project> project = projectService.findProjectById(worklog.getProjectId());
        if (project.isEmpty()) {
            log.warn("There is no project with id: " + worklog.getProjectId());
            throw new NoProjectFoundException("There is no project with id: " + worklog.getProjectId());
        }
        if (worklogService.isWorklogsDateInTheFuture(worklog)) {
            throw new WorklogDateInFutureException("You cannot add worklogs in the future (unless you are Jesus Christ)!");
        }
        worklogService.updateWorklog(worklog);
    }

    @Override
    public void updateWorklog(UUID accessingEmployeeId, Worklog worklog) throws EmployeeNotFoundException, NoProjectFoundException, WorklogNotFoundException, InvalidArgumentException {
        Optional<Employee> accessingEmployeeOptional = employeeService.findEmployeeById(accessingEmployeeId);
        if (accessingEmployeeOptional.isEmpty()) {
            log.warn("Employee not found for ID: " + accessingEmployeeId);
            throw new EmployeeNotFoundException("Employee not found for the given ID");
        }
        Optional<Worklog> worklogOptional = worklogService.findWorklogById(worklog.getId());
        if (worklogOptional.isEmpty()) {
            log.warn("Wrong worklog id!");
            throw new WorklogNotFoundException("There is no worklog with id:" + worklog.getId());
        }
        Optional<Employee> employeeOptional = employeeService.findEmployeeById(worklog.getEmployeeId());
        if (employeeOptional.isEmpty()) {
            log.warn("There is no employee with id: " + worklog.getEmployeeId());
            throw new EmployeeNotFoundException("There is no employee with id: " + worklog.getEmployeeId());
        }
        Optional<Project> project = projectService.findProjectById(worklog.getProjectId());
        if (project.isEmpty()) {
            log.warn("There is no project with id: " + worklog.getProjectId());
            throw new NoProjectFoundException("There is no project with id: " + worklog.getProjectId());
        }
        if (worklogService.isWorklogsDateInTheFuture(worklog)) {
            throw new WorklogDateInFutureException("You cannot add worklogs in the future (unless you are Jesus Christ)!");
        }
        if (worklog.getEmployeeId().equals(accessingEmployeeId)) {
            Worklog existingWorklog = worklogOptional.get();
            if (worklogService.isWorklogsDateLocked(existingWorklog)) {
                throw new WorklogDateLockedException("You cannot edit worklogs that are more than 3 days in the past!");
            }
            if (worklogService.isWorklogsDateLocked(worklog)) {
                throw new WorklogDateLockedException("You cannot put dates of worklogs that are more than 3 days in the past!");
            }
            worklogService.updateWorklog(worklog);
        } else {
            Employee accessingEmployee = accessingEmployeeOptional.get();
            Employee employee = employeeOptional.get();
            if (employeeHierarchyService.isSubordinate(accessingEmployee, employee)) {
                worklogService.updateWorklog(worklog);
            } else {
                log.warn("User with ID " + accessingEmployeeId + " is not a superior to user with ID " + worklog.getEmployeeId());
                throw new NotASubordinateException("You do not have permission to change this user's worklogs!");
            }
        }

    }

    @Override
    @Transactional
    public List<Worklog> listEmployeeWorklogs(UUID employeeId) throws EmployeeNotFoundException {
        Optional<Employee> employee = employeeService.findEmployeeById(employeeId);
        if (employee.isEmpty()) {
            log.warn("There is no employee with id: " + employeeId);
            throw new EmployeeNotFoundException("There is no employee with id: " + employeeId);
        }
        return worklogService.findWorklogByEmployeeId(employeeId);
    }

    @Override
    @Transactional
    public List<Worklog> listEmployeeWorklogsByCreationDate(UUID employeeId, LocalDate creationDate, int page) throws EmployeeNotFoundException {
        Optional<Employee> employee = employeeService.findEmployeeById(employeeId);
        if (employee.isEmpty()) {
            log.warn("There is no employee with id: " + employeeId);
            throw new EmployeeNotFoundException("There is no employee with id: " + employeeId);
        }
        // TODO: Add necessary checks for creationDate: it shouldn't be in the future
        return worklogService.findWorklogByEmployeeIdAndCreationDate(employeeId, creationDate, page);
    }

    @Override
    public List<Worklog> listWorklogByAnyCriteria(Map<String, String[]> criteria) {
        return worklogService.listWorklogByAnyCriteria(criteria);
    }

    @Override
    public List<Worklog> listWorklogByAnyCriteria(UUID accessingEmployeeId, Map<String, String[]> criteria) {
        Optional<Employee> employeeOptional = employeeService.findEmployeeById(accessingEmployeeId);
        if (employeeOptional.isEmpty()) {
            log.warn("Cannot proceed with listWorklogByAnyCriteria. Searching employee not found for ID: " + accessingEmployeeId + "!");
            throw new EmployeeNotFoundException("Employee not found for the given ID!");
        }
        List<UUID> searchableEmployeesIDs = employeeHierarchyService.findAllSubordinates(employeeOptional.get())
                .stream().map(Employee::getId).collect(Collectors.toList());
        searchableEmployeesIDs.add(accessingEmployeeId);
        List<Worklog> foundWorklogs = listWorklogByAnyCriteria(criteria);
        return foundWorklogs.stream()
                .filter(w -> searchableEmployeesIDs.contains(w.getEmployeeId()))
                .toList();
    }

    @Override
    public List<Worklog> listWorklogByAllCriteria(Map<String, String[]> criteria) {
        return worklogService.listWorklogByAllCriteria(criteria);
    }

    @Override
    public List<Worklog> listWorklogByAllCriteria(UUID accessingEmployeeId, Map<String, String[]> criteria) {
        Optional<Employee> employeeOptional = employeeService.findEmployeeById(accessingEmployeeId);
        if (employeeOptional.isEmpty()) {
            log.warn("Cannot proceed with listWorklogByAnyCriteria. Searching employee not found for ID: " + accessingEmployeeId + "!");
            throw new EmployeeNotFoundException("Employee not found for the given ID!");
        }
        List<UUID> searchableEmployeesIDs = employeeHierarchyService.findAllSubordinates(employeeOptional.get())
                .stream().map(Employee::getId).collect(Collectors.toList());
        searchableEmployeesIDs.add(accessingEmployeeId);
        List<Worklog> foundWorklogs = listWorklogByAllCriteria(criteria);
        return foundWorklogs.stream()
                .filter(w -> searchableEmployeesIDs.contains(w.getEmployeeId()))
                .toList();
    }

    @Override
    @Transactional
    public List<Worklog> getOwnWorklogs(UUID id) throws EmployeeNotFoundException {
        return worklogService.findWorklogByEmployeeId(id);
    }

    @Override
    @Transactional
    public List<Worklog> findSubordinateWorklogs(UUID loggedUser, UUID subordinateId) throws EmployeeNotFoundException, NotReachableNodeException {
        Optional<Employee> parent = employeeService.findEmployeeById(loggedUser);
        if (parent.isEmpty()) {
            log.warn("Wrong id of logged user");
            throw new EmployeeNotFoundException("There is no user with id: " + loggedUser);
        }
        Optional<Employee> child = employeeService.findEmployeeById(subordinateId);
        if (child.isEmpty()) {
            log.warn("Wrong id of subordinate");
            throw new EmployeeNotFoundException("There is no user with id: " + subordinateId);
        }
        if (!employeeHierarchyService.isSubordinate(parent.get(), child.get())) {
            log.warn("Employee with id: " + subordinateId + " is not subordinate of the Employee with id: " + loggedUser);
            throw new NotReachableNodeException("Employee with id: " + subordinateId + " is not subordinate of the Employee with id: " + loggedUser);
        }
        return worklogService.findWorklogByEmployeeId(subordinateId);
    }

    @Override
    public void deleteWorklogById(UUID id) {
        worklogService.deleteWorklogById(id);
    }

    @Override
    @Transactional
    public List<WorklogType> listWorklogTypes() {
        return worklogService.listWorklogTypes();
    }

    @Override
    public int findNumberOfWorklogs(UUID employeeId, LocalDate creationDate) {
        return worklogService.findNumberOfWorklogs(employeeId, creationDate);
    }

}
