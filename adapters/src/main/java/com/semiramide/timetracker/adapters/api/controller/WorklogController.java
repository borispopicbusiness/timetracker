package com.semiramide.timetracker.adapters.api.controller;


import com.semiramide.timetracker.adapters.api.dto.WorklogDtoAPI;
import com.semiramide.timetracker.adapters.api.mapper.WorklogMapperAPI;
import com.semiramide.timetracker.core.entity.Employee;
import com.semiramide.timetracker.core.entity.Worklog;
import com.semiramide.timetracker.core.entity.enums.WorklogType;
import com.semiramide.timetracker.core.exception.*;
import com.semiramide.timetracker.core.service.EmployeeService;
import com.semiramide.timetracker.core.usecase.EmployeeUseCase;
import com.semiramide.timetracker.core.usecase.WorklogUseCase;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("worklog")
@RequiredArgsConstructor

public class WorklogController {

    private final EmployeeService employeeService;
    private final WorklogUseCase worklogUseCase;
    private final EmployeeUseCase employeeUseCase;


    @PostMapping
    public WorklogDtoAPI createWorklog(Principal principal, @RequestBody WorklogDtoAPI worklogDtoAPI, HttpServletRequest request) throws NoProjectFoundException, EmployeeNotFoundException, InvalidArgumentException {
        Optional<Employee> employeeOptional = employeeUseCase.findEmployeeByPrincipalId(principal.getName());
        if (employeeOptional.isEmpty()) {
            throw new EmployeeNotFoundException("Employee not found for the given principal id!");
        }
        Employee employee = employeeOptional.get();
        if (worklogDtoAPI.getEmployeeId() == null) {
            worklogDtoAPI.setEmployeeId(employee.getId());
        }
        Worklog worklog;
        if (request.isUserInRole("ROLE_ROLE_ADMIN")) {
            worklog = worklogUseCase.addWorklog(WorklogMapperAPI.INSTANCE.worklogDtoAPIToWorklog(worklogDtoAPI));
        } else {
            worklog = worklogUseCase.addWorklog(employee.getId(), WorklogMapperAPI.INSTANCE.worklogDtoAPIToWorklog(worklogDtoAPI));
        }
        return WorklogMapperAPI.INSTANCE.worklogToWorklogDtoAPI(worklog);
    }

    @PutMapping
    public WorklogDtoAPI updateWorklog(Principal principal, @RequestBody WorklogDtoAPI worklogDtoAPI, HttpServletRequest request) throws NoProjectFoundException, EmployeeNotFoundException, WorklogNotFoundException, InvalidArgumentException {
        Optional<Employee> employeeOptional = employeeUseCase.findEmployeeByPrincipalId(principal.getName());
        if (employeeOptional.isEmpty()) {
            throw new EmployeeNotFoundException("Employee not found for the given principal id!");
        }
        if (request.isUserInRole("ROLE_ROLE_ADMIN")) {
            worklogUseCase.updateWorklog(WorklogMapperAPI.INSTANCE.worklogDtoAPIToWorklog(worklogDtoAPI));
        } else {
            worklogUseCase.updateWorklog(employeeOptional.get().getId(), WorklogMapperAPI.INSTANCE.worklogDtoAPIToWorklog(worklogDtoAPI));
        }
        return worklogDtoAPI;
    }

    @GetMapping
    public List<WorklogDtoAPI> listSubordinateWorklogs(Principal principal, @RequestParam(name = "employee-id") UUID subordinateId) throws EmployeeNotFoundException, NotReachableNodeException {
        Optional<Employee> employeeOptional = employeeUseCase.findEmployeeByPrincipalId(principal.getName());
        if (employeeOptional.isEmpty()) {
            throw new EmployeeNotFoundException("Employee not found for the given principal id!");
        }
        Employee employee = employeeOptional.get();
        List<Worklog> worklogs = worklogUseCase.findSubordinateWorklogs(employee.getId(), subordinateId);
        return WorklogMapperAPI.INSTANCE.worklogListToWorklogDtoAPIList(worklogs);
    }

    @GetMapping("/own")
    public List<WorklogDtoAPI> listOwnWorklogs(Principal principal, @RequestParam(name = "creation-date", required = false) LocalDate creationDate, @RequestParam(name = "page", required = false) int page) throws EmployeeNotFoundException {
        Optional<Employee> employeeOptional = employeeUseCase.findEmployeeByPrincipalId(principal.getName());
        if (employeeOptional.isEmpty()) {
            throw new EmployeeNotFoundException("Employee not found for the given principal id!");
        }
        Employee employee = employeeOptional.get();
        List<Worklog> worklogs = null;
        if (creationDate != null) {
            worklogs = worklogUseCase.listEmployeeWorklogsByCreationDate(employee.getId(), creationDate, page);
        } else {
            worklogs = worklogUseCase.listEmployeeWorklogs(employee.getId());
        }

        return WorklogMapperAPI.INSTANCE.worklogListToWorklogDtoAPIList(worklogs);
    }

    @GetMapping("/length")
    public int findNumberOfWorklogs(Principal principal, @RequestParam(name = "creation-date", required = false) LocalDate creationDate) throws EmployeeNotFoundException {
        Optional<Employee> employeeOptional = employeeUseCase.findEmployeeByPrincipalId(principal.getName());
        if (employeeOptional.isEmpty()) {
            throw new EmployeeNotFoundException("Employee not found for the given principal id!");
        }
        Employee employee = employeeOptional.get();
        int size;
        if (creationDate != null) {
            size = worklogUseCase.findNumberOfWorklogs(employee.getId(), creationDate);
        } else {
            size = -1;
        }
        return size;
    }

    @GetMapping("/any")
    public List<WorklogDtoAPI> listWorklogByAnyCriteria(Principal principal, HttpServletRequest request) {
        if (request.isUserInRole("ROLE_ROLE_ADMIN")) {
            return WorklogMapperAPI.INSTANCE
                    .worklogListToWorklogDtoAPIList(worklogUseCase.listWorklogByAnyCriteria(request.getParameterMap()));
        }
        Optional<Employee> employeeOptional = employeeUseCase.findEmployeeByPrincipalId(principal.getName());
        if (employeeOptional.isEmpty()) {
            throw new EmployeeNotFoundException("Employee not found for the given principal id!");
        }
        Employee loggedInEmployee = employeeOptional.get();
        List<Worklog> filteredWorklogs = worklogUseCase.listWorklogByAnyCriteria(loggedInEmployee.getId(), request.getParameterMap());
        return WorklogMapperAPI.INSTANCE.worklogListToWorklogDtoAPIList(filteredWorklogs);
    }

    @GetMapping("/all")
    public List<WorklogDtoAPI> listWorklogByAllCriteria(Principal principal, HttpServletRequest request) {
        if (request.isUserInRole("ROLE_ROLE_ADMIN")) {
            return WorklogMapperAPI.INSTANCE
                    .worklogListToWorklogDtoAPIList(worklogUseCase.listWorklogByAllCriteria(request.getParameterMap()));
        }
        Optional<Employee> employeeOptional = employeeUseCase.findEmployeeByPrincipalId(principal.getName());
        if (employeeOptional.isEmpty()) {
            throw new EmployeeNotFoundException("Employee not found for the given principal id!");
        }
        Employee loggedInEmployee = employeeOptional.get();
        List<Worklog> filteredWorklogs = worklogUseCase.listWorklogByAllCriteria(loggedInEmployee.getId(), request.getParameterMap());
        return WorklogMapperAPI.INSTANCE.worklogListToWorklogDtoAPIList(filteredWorklogs);
    }

    @DeleteMapping("/{id}")
    public void deleteWorklog(@PathVariable(name = "id") UUID id) {
        worklogUseCase.deleteWorklogById(id);
    }

    @GetMapping("/types")
    public List<WorklogType> listWorklogTypes() {
        return worklogUseCase.listWorklogTypes();
    }

}
