package com.semiramide.timetracker.adapters.api.controller;

import com.semiramide.timetracker.adapters.api.dto.EmployeeDtoAPI;
import com.semiramide.timetracker.adapters.api.dto.ProjectEmployeeDtoAPI;
import com.semiramide.timetracker.adapters.api.mapper.EmployeeMapperAPI;
import com.semiramide.timetracker.adapters.api.mapper.ProjectEmployeeMapperAPI;
import com.semiramide.timetracker.core.entity.Employee;
import com.semiramide.timetracker.core.entity.ProjectEmployee;
import com.semiramide.timetracker.core.usecase.EmployeeUseCase;
import com.semiramide.timetracker.core.usecase.ProjectEmployeeUseCase;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("project-employees")
@RequiredArgsConstructor
public class ProjectEmployeeController {

    private final ProjectEmployeeUseCase projectEmployeeUseCase;
    private final EmployeeUseCase employeeUseCase;

    @GetMapping
    public List<ProjectEmployeeDtoAPI> findAll() {
        return ProjectEmployeeMapperAPI.INSTANCE.projectEmployeeListToProjectEmployeeDtoAPIList(
                projectEmployeeUseCase.findAll());
    }

    @PostMapping
    public ProjectEmployeeDtoAPI save(@RequestBody ProjectEmployeeDtoAPI projectEmployeeDtoAPI) {
        ProjectEmployee savedEntity =
                projectEmployeeUseCase.save(
                        ProjectEmployeeMapperAPI.INSTANCE.projectEmployeeDtoAPIToProjectEmployee(
                                projectEmployeeDtoAPI));
        return ProjectEmployeeMapperAPI.INSTANCE.projectEmployeeToProjectEmployeeDtoAPI(savedEntity);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable(name = "id") UUID id) {
        projectEmployeeUseCase.deleteById(id);
    }

    @GetMapping("subordinates")
    public List<ProjectEmployeeDtoAPI> findByLoggedUser(Principal principal) {
        Optional<Employee> employee = employeeUseCase.findEmployeeByPrincipalId(principal.getName());
        if ( employee.isEmpty() ) {
            return null;
        }
        List<ProjectEmployee> subordinates =
                projectEmployeeUseCase.findByLoggedUser(employee.get().getId());
        return ProjectEmployeeMapperAPI.INSTANCE.projectEmployeeListToProjectEmployeeDtoAPIList(
                subordinates);
    }

    @GetMapping("/projectId/{projectId}")
    public List<ProjectEmployeeDtoAPI> findByProjectId(@PathVariable(name = "projectId") UUID id) {
        return ProjectEmployeeMapperAPI.INSTANCE.projectEmployeeListToProjectEmployeeDtoAPIList(
                projectEmployeeUseCase.findByProjectId(id));
    }

    @DeleteMapping
    public void deleteByProjectIdAndEmployeeId(
            @RequestParam(name = "projectId") String projectId,
            @RequestParam(name = "employeeId") String employeeId) {
        UUID pid = UUID.fromString(projectId);
        UUID eid = UUID.fromString(employeeId);
        projectEmployeeUseCase.deleteByProjectIdAndEmployeeId(pid, eid);
    }

    @GetMapping("/non-assigned-employees/{projectId}")
    public List<EmployeeDtoAPI> findNonAssignedEmployees(@PathVariable(name = "projectId") UUID id) {
        return EmployeeMapperAPI.INSTANCE.employeeListToEmployeeDtoAPIList(
                projectEmployeeUseCase.findNonAssignedEmployees(id));
    }

    @GetMapping("/assigned-employees/{projectId}")
    public List<EmployeeDtoAPI> findAssignedEmployees(@PathVariable(name = "projectId") UUID id) {
        return EmployeeMapperAPI.INSTANCE.employeeListToEmployeeDtoAPIList(
                projectEmployeeUseCase.findAssignedEmployees(id));
    }
}
