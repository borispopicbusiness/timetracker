package com.semiramide.timetracker.adapters.api.controller;

import com.semiramide.timetracker.adapters.api.dto.EmployeeDtoAPI;
import com.semiramide.timetracker.adapters.api.mapper.EmployeeMapperAPI;
import com.semiramide.timetracker.adapters.api.request.AssignSubordinateRequest;
import com.semiramide.timetracker.adapters.api.request.ChangePasswordRequest;
import com.semiramide.timetracker.core.entity.Employee;
import com.semiramide.timetracker.core.event.AppEventPublisher;
import com.semiramide.timetracker.core.exception.*;
import com.semiramide.timetracker.core.service.EmployeeHierarchyService;
import com.semiramide.timetracker.core.usecase.EmployeeUseCase;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("employee")
@RequiredArgsConstructor
@Data
public class EmployeeController {

    private final EmployeeUseCase employeeUseCase;
    private final EmployeeHierarchyService employeeHierarchyService;
    private final AppEventPublisher eventPublisher;

    @ResponseBody
    @PostMapping
    public EmployeeDtoAPI createEmployee(@RequestBody EmployeeDtoAPI employeeDtoAPI)
            throws EmailAlreadyExistsException {
        Employee employee = EmployeeMapperAPI.INSTANCE.employeeDtoAPIToEmployee(employeeDtoAPI);
        Employee createdEmployee = employeeUseCase.createEmployee(employee);
        return EmployeeMapperAPI.INSTANCE.employeeToEmployeeDtoAPI(createdEmployee);
    }

    @PutMapping
    public EmployeeDtoAPI updateEmployee(@RequestBody EmployeeDtoAPI employeeDtoAPI)
            throws EmailAlreadyExistsException, EmployeeNotFoundException {
        Employee employee =
                employeeUseCase.updateEmployee(
                        EmployeeMapperAPI.INSTANCE.employeeDtoAPIToEmployee(employeeDtoAPI));
        if ( employee != null ) {
            return EmployeeMapperAPI.INSTANCE.employeeToEmployeeDtoAPI(employee);
        }
        return null;
    }

    @GetMapping
    public List<EmployeeDtoAPI> findAll() {
        List<Employee> employees = employeeUseCase.findAllEmployees();
        return EmployeeMapperAPI.INSTANCE.employeeListToEmployeeDtoAPIList(employees);
    }

    @PostMapping("/assign-subordinate")
    public void assignSubordinate(@RequestBody AssignSubordinateRequest request)
            throws EmployeeNotFoundException,
            CycleDetectedException,
            InvalidArgumentException,
            AlreadyASubordinateException {
        employeeUseCase.assignSubordinate(request.getParentId(), request.getChildId());
    }

    @DeleteMapping("/subordinate")
    public void unAssignSubordinate(
            @RequestParam(name = "parent-id") String parentId,
            @RequestParam(name = "child-id") String childId)
            throws NotASubordinateException, EmployeeNotFoundException {
        employeeUseCase.unAssignSubordinate(UUID.fromString(parentId), UUID.fromString(childId));
    }

    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable(name = "id") UUID id) throws EmployeeNotFoundException {
        employeeUseCase.removeEmployee(id);
    }

    @GetMapping("/subordinates")
    public List<EmployeeDtoAPI> findAllSubordinates(
            Principal principal, @RequestParam(name = "employee-id", required = false) UUID employeeId)
            throws EmployeeNotFoundException {
        if ( employeeId == null ) {
            Optional<Employee> employeeOptional =
                    employeeUseCase.findEmployeeByPrincipalId(principal.getName());
            if ( employeeOptional.isEmpty() ) {
                throw new EmployeeNotFoundException("Employee not found for the given principal id!");
            }
            Employee employee = employeeOptional.get();
            List<Employee> subordinates = employeeHierarchyService.findAllSubordinates(employee);
            return EmployeeMapperAPI.INSTANCE.employeeListToEmployeeDtoAPIList(subordinates);
        } else {
            Optional<Employee> employeeOptional = employeeUseCase.findById(employeeId);
            if ( employeeOptional.isEmpty() ) {
                throw new EmployeeNotFoundException("Employee not found for the given principal id!");
            }
            List<Employee> subordinates = employeeUseCase.listImmediateSubordinates(employeeId);
            return EmployeeMapperAPI.INSTANCE.employeeListToEmployeeDtoAPIList(subordinates);
        }
    }

    @GetMapping("/subordinates/immediate")
    public List<EmployeeDtoAPI> listImmediateSubordinates(
            Principal principal,
            @RequestParam(name = "employee-id", required = false) String employeeIdString)
            throws EmployeeNotFoundException {
        UUID employeeId = UUID.fromString(employeeIdString);
        if ( employeeId == null ) {
            Optional<Employee> employeeOptional =
                    employeeUseCase.findEmployeeByPrincipalId(principal.getName());
            if ( employeeOptional.isEmpty() ) {
                throw new EmployeeNotFoundException("Employee not found for the given principal id!");
            }
            Employee employee = employeeOptional.get();
            List<Employee> subordinates = employeeUseCase.listImmediateSubordinates(employee.getId());
            return EmployeeMapperAPI.INSTANCE.employeeListToEmployeeDtoAPIList(subordinates);
        } else {
            Optional<Employee> employeeOptional = employeeUseCase.findById(employeeId);
            if ( employeeOptional.isEmpty() ) {
                throw new EmployeeNotFoundException("Employee not found for the given principal id!");
            }
            List<Employee> subordinates = employeeUseCase.listImmediateSubordinates(employeeId);
            return EmployeeMapperAPI.INSTANCE.employeeListToEmployeeDtoAPIList(subordinates);
        }
    }

    @GetMapping("/superiors/immediate")
    public List<EmployeeDtoAPI> listImmediateSuperiors(
            Principal principal, @RequestParam(name = "employee-id", required = false) UUID employeeId)
            throws EmployeeNotFoundException {
        if ( employeeId == null ) {
            Optional<Employee> employeeOptional =
                    employeeUseCase.findEmployeeByPrincipalId(principal.getName());
            if ( employeeOptional.isEmpty() ) {
                throw new EmployeeNotFoundException("Employee not found for the given principal id!");
            }
            Employee employee = employeeOptional.get();
            List<Employee> subordinates = employeeUseCase.listImmediateSuperiors(employee.getId());
            return EmployeeMapperAPI.INSTANCE.employeeListToEmployeeDtoAPIList(subordinates);
        } else {
            Optional<Employee> employeeOptional = employeeUseCase.findById(employeeId);
            if ( employeeOptional.isEmpty() ) {
                throw new EmployeeNotFoundException("Employee not found for the given principal id!");
            }
            List<Employee> subordinates = employeeUseCase.listImmediateSuperiors(employeeId);
            return EmployeeMapperAPI.INSTANCE.employeeListToEmployeeDtoAPIList(subordinates);
        }
    }

    @GetMapping("/available-connections")
    public List<EmployeeDtoAPI> listAllNotConnectedEmployees(
            @RequestParam(name = "employee-id") String employeeIdString)
            throws EmployeeNotFoundException {
        UUID employeeId = UUID.fromString(employeeIdString);
        return EmployeeMapperAPI.INSTANCE.employeeListToEmployeeDtoAPIList(
                employeeUseCase.listNotConnectedEmployees(employeeId));
    }

    @GetMapping("/me")
    public EmployeeDtoAPI findLoggedUser(Principal principal) throws EmployeeNotFoundException {
        Optional<Employee> employeeOptional =
                employeeUseCase.findEmployeeByPrincipalId(principal.getName());
        if ( employeeOptional.isEmpty() ) {
            throw new EmployeeNotFoundException("Employee not found for the given principal id!");
        }
        Employee employee = employeeOptional.get();
        return EmployeeMapperAPI.INSTANCE.employeeToEmployeeDtoAPI(employee);
    }

    @GetMapping("/{id}")
    public EmployeeDtoAPI findById(@PathVariable(name = "id") UUID id)
            throws EmployeeNotFoundException {
        Optional<Employee> employeeOptional = employeeUseCase.findById(id);
        if ( employeeOptional.isEmpty() ) {
            throw new EmployeeNotFoundException("Employee not found for the given id!");
        }
        Employee employee = employeeOptional.get();
        return EmployeeMapperAPI.INSTANCE.employeeToEmployeeDtoAPI(employee);
    }

    @PutMapping("/me/change-password")
    public void changePassword(
            Principal principal, @RequestBody ChangePasswordRequest changePasswordRequest)
            throws EmployeeNotFoundException {
        employeeUseCase.changePassword(principal.getName(), changePasswordRequest.getNewPassword());
    }
}
