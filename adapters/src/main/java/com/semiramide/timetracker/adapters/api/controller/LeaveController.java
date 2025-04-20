package com.semiramide.timetracker.adapters.api.controller;

import com.semiramide.timetracker.adapters.api.dto.LeaveDtoAPI;
import com.semiramide.timetracker.adapters.api.mapper.LeaveMapperAPI;
import com.semiramide.timetracker.core.entity.Employee;
import com.semiramide.timetracker.core.entity.Leave;
import com.semiramide.timetracker.core.entity.enums.LeaveStatus;
import com.semiramide.timetracker.core.entity.enums.LeaveType;
import com.semiramide.timetracker.core.exception.EmployeeNotFoundException;
import com.semiramide.timetracker.core.exception.FreeDaysLeftException;
import com.semiramide.timetracker.core.usecase.EmployeeUseCase;
import com.semiramide.timetracker.core.usecase.LeaveUseCase;
import com.semiramide.timetracker.core.usecase.NotificationUseCase;
import jakarta.servlet.http.HttpServletRequest;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("leave")
@RequiredArgsConstructor
public class LeaveController {

    private final LeaveUseCase leaveUseCase;
    private final EmployeeUseCase employeeUseCase;
    private final NotificationUseCase notificationUseCase;

    @PostMapping
    public LeaveDtoAPI requestLeave(@RequestBody LeaveDtoAPI leaveDtoAPI, Principal principal)
            throws EmployeeNotFoundException, FreeDaysLeftException {
        Optional<Employee> employeeOptional =
                employeeUseCase.findEmployeeByPrincipalId(principal.getName());
        if ( employeeOptional.isEmpty() ) {
            throw new EmployeeNotFoundException(
                    "Employee with principal id: " + principal.getName() + " does not exist.");
        }
        leaveDtoAPI.setEmployeeId(employeeOptional.get().getId());
        Leave leave =
                leaveUseCase.requestLeave(LeaveMapperAPI.INSTANCE.leaveDtoAPIToLeave(leaveDtoAPI));

        Employee employee = employeeOptional.get();
        List<Employee> superiors = employeeUseCase.listImmediateSuperiors(employee.getId());
        notificationUseCase.sendLeaveRequestMail(employee, superiors);

        return LeaveMapperAPI.INSTANCE.leaveToLeaveDtoAPI(leave);
    }

    @PutMapping("/approve")
    public void approveLeave(@RequestBody LeaveDtoAPI leaveDtoAPI, Principal principal)
            throws EmployeeNotFoundException, FreeDaysLeftException {
        Optional<Employee> employeeOptional =
                employeeUseCase.findEmployeeByPrincipalId(principal.getName());
        if ( employeeOptional.isEmpty() ) {
            throw new EmployeeNotFoundException(
                    "Employee with principal id: " + principal.getName() + " does not exist.");
        }

        leaveDtoAPI.setResponderId(employeeOptional.get().getId());
        Leave leave = LeaveMapperAPI.INSTANCE.leaveDtoAPIToLeave(leaveDtoAPI);

        Optional<Employee> receiverOptional = employeeUseCase.findById(leave.getEmployeeId());
        if ( receiverOptional.isEmpty() ) {
            throw new EmployeeNotFoundException(
                    "Employee with  id: " + leave.getEmployeeId() + " does not exist.");
        }
        notificationUseCase.sendLeaveResponseMail(employeeOptional.get(), receiverOptional.get());
        leaveUseCase.approveLeave(leave);
    }

    @PutMapping("/deny")
    public void denyLeave(@RequestBody LeaveDtoAPI leaveDtoAPI, Principal principal)
            throws EmployeeNotFoundException {
        Optional<Employee> employeeOptional =
                employeeUseCase.findEmployeeByPrincipalId(principal.getName());
        if ( employeeOptional.isEmpty() ) {
            throw new EmployeeNotFoundException(
                    "Employee with pricipal id: " + principal.getName() + " does not exist.");
        }
        leaveDtoAPI.setResponderId(employeeOptional.get().getId());
        Leave leave = LeaveMapperAPI.INSTANCE.leaveDtoAPIToLeave(leaveDtoAPI);

        Optional<Employee> receiverOptional = employeeUseCase.findById(leave.getEmployeeId());
        if ( receiverOptional.isEmpty() ) {
            throw new EmployeeNotFoundException(
                    "Employee with  id: " + leave.getEmployeeId() + " does not exist.");
        }
        notificationUseCase.sendLeaveResponseMail(employeeOptional.get(), receiverOptional.get());
        leaveUseCase.denyLeave(leave);
    }

    @GetMapping
    public List<LeaveDtoAPI> findAll() {
        List<Leave> leaves = leaveUseCase.findAll();
        return LeaveMapperAPI.INSTANCE.leaveListToLeaveDtoAPIList(leaves);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable(name = "id") UUID id) {
        leaveUseCase.deleteById(id);
    }

    @GetMapping("/types")
    public List<LeaveType> listTypes() {
        return leaveUseCase.listLeaveTypes();
    }

    @GetMapping("/status")
    public List<LeaveStatus> listStatus() {
        return leaveUseCase.listLeaveStatus();
    }

    @GetMapping("/subordinates")
    List<LeaveDtoAPI> findLeavesOfSubordinates(Principal principal) throws EmployeeNotFoundException {
        Optional<Employee> employeeOptional =
                employeeUseCase.findEmployeeByPrincipalId(principal.getName());
        if ( employeeOptional.isEmpty() ) {
            throw new EmployeeNotFoundException("Employee not found for the given principal id!");
        }
        Employee employee = employeeOptional.get();
        List<Leave> leaves = leaveUseCase.findLeavesOfSubordinates(employee.getId());
        return LeaveMapperAPI.INSTANCE.leaveListToLeaveDtoAPIList(leaves);
    }

    @GetMapping("/own")
    List<LeaveDtoAPI> findOwnLeaves(Principal principal) throws EmployeeNotFoundException {
        Optional<Employee> employeeOptional =
                employeeUseCase.findEmployeeByPrincipalId(principal.getName());
        if ( employeeOptional.isEmpty() ) {
            throw new EmployeeNotFoundException("Employee not found for the given principal id!");
        }
        Employee employee = employeeOptional.get();
        List<Leave> leaves = leaveUseCase.findByEmployeeId(employee.getId());
        return LeaveMapperAPI.INSTANCE.leaveListToLeaveDtoAPIList(leaves);
    }

    @GetMapping("/subordinates-leaves")
    List<LeaveDtoAPI> findAllSubordinatesLeaves(Principal principal)
            throws EmployeeNotFoundException {
        Optional<Employee> employeeOptional =
                employeeUseCase.findEmployeeByPrincipalId(principal.getName());
        if ( employeeOptional.isEmpty() ) {
            throw new EmployeeNotFoundException("Employee not found for the given principal id!");
        }
        Employee employee = employeeOptional.get();
        List<Leave> leaves = leaveUseCase.findAllSubordinatesLeaves(employee);
        return LeaveMapperAPI.INSTANCE.leaveListToLeaveDtoAPIList(leaves);
    }

    @PutMapping()
    public LeaveDtoAPI updateLeave(
            @RequestBody LeaveDtoAPI leaveDtoAPI,
            Principal principal,
            HttpServletRequest httpServletRequest)
            throws EmployeeNotFoundException {
        JwtAuthenticationToken principal1 = (JwtAuthenticationToken) principal;
        boolean roleRoleAdmin =
                principal1.getAuthorities().stream()
                        .map(granted -> granted.getAuthority().equals("ROLE_ROLE_ADMIN"))
                        .toList()
                        .contains(true);
        if ( roleRoleAdmin ) {
            Leave leave = LeaveMapperAPI.INSTANCE.leaveDtoAPIToLeave(leaveDtoAPI);
            leaveUseCase.deleteById(leave.getId());
            return LeaveMapperAPI.INSTANCE.leaveToLeaveDtoAPI(leaveUseCase.update(leave));
        }
        Optional<Employee> employeeOptional =
                employeeUseCase.findEmployeeByPrincipalId(principal.getName());
        if ( employeeOptional.isEmpty() ) {
            throw new EmployeeNotFoundException(
                    "Employee with pricipal id: " + principal.getName() + " does not exist.");
        }
        Leave leave = LeaveMapperAPI.INSTANCE.leaveDtoAPIToLeave(leaveDtoAPI);
        Leave updatedLeave = leaveUseCase.update(leave);

        Employee employee = employeeOptional.get();

        List<Employee> superiors = employeeUseCase.listImmediateSuperiors(employee.getId());
        notificationUseCase.sendLeaveDetailsUpdatedMail(employee, superiors);

        return LeaveMapperAPI.INSTANCE.leaveToLeaveDtoAPI(updatedLeave);
    }
}
