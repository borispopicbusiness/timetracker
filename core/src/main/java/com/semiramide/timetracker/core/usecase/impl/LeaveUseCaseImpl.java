package com.semiramide.timetracker.core.usecase.impl;

import com.semiramide.timetracker.core.entity.Employee;
import com.semiramide.timetracker.core.entity.Leave;
import com.semiramide.timetracker.core.entity.enums.LeaveStatus;
import com.semiramide.timetracker.core.entity.enums.LeaveType;
import com.semiramide.timetracker.core.exception.*;
import com.semiramide.timetracker.core.service.EmployeeHierarchyService;
import com.semiramide.timetracker.core.service.EmployeeService;
import com.semiramide.timetracker.core.service.LeaveService;
import com.semiramide.timetracker.core.usecase.LeaveUseCase;
import com.semiramide.timetracker.core.exception.*;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import lombok.Builder;

@Builder
public class LeaveUseCaseImpl implements LeaveUseCase {
    private final LeaveService leaveService;

    private final EmployeeHierarchyService employeeHierarchyService;

    private final EmployeeService employeeService;

    @Override
    @Transactional
    public Leave requestLeave(Leave leave) throws EmployeeNotFoundException, FreeDaysLeftException {
        Optional<Employee> employeeOptional = employeeService.findEmployeeById(leave.getEmployeeId());
        if ( employeeOptional.isEmpty() ) {
            throw new EmployeeNotFoundException("There is no employee with id: " + leave.getEmployeeId());
        }
        int numberOfRequestedDays =
                (int) ChronoUnit.DAYS.between(leave.getStartTime(), leave.getEndTime());
        Employee employee = employeeOptional.get();
        if ( employee.getFreeDaysLeft() < numberOfRequestedDays ) {
            throw new FreeDaysLeftException(
                    "Employee "
                            + employee.getFirstName()
                            + " "
                            + employee.getLastName()
                            + " requested"
                            + " more free days than he has.");
        }
        if ( leaveService.isStartInThePast(leave) ) {
            throw new LeaveStartsInThePastException("Requested leave's start date is in the past!");
        }
        if ( !leaveService.isStartBeforeOrSameAsEnd(leave) ) {
            throw new LeaveStartAfterEndException("Leave start date cannot be after end date!");
        }
        if ( leaveService.hasOverlaps(leave) ) {
            throw new LeavesOverlapException("Requested leave has overlap with some other leave!");
        }
        leave.setLeaveStatus(LeaveStatus.OPEN);
        leave.setRequestDate(LocalDate.now());
        return leaveService.addLeave(leave);
    }

    @Override
    @Transactional
    public void approveLeave(Leave leave) throws EmployeeNotFoundException, FreeDaysLeftException {
        Optional<Employee> employeeOptional = employeeService.findEmployeeById(leave.getEmployeeId());
        if ( employeeOptional.isEmpty() ) {
            throw new EmployeeNotFoundException("There is no employee with id: " + leave.getEmployeeId());
        }
        Integer numberOfRequestedDays =
                (int) ChronoUnit.DAYS.between(leave.getStartTime(), leave.getEndTime());
        Employee employee = employeeOptional.get();
        if ( employee.getFreeDaysLeft() < numberOfRequestedDays ) {
            throw new FreeDaysLeftException(
                    "Employee "
                            + employee.getFirstName()
                            + " "
                            + employee.getLastName()
                            + " requested"
                            + " more free days than he has left.");
        }
        leaveService.updateLeave(leave);
        employeeService.calculateFreeDaysLeft(numberOfRequestedDays, employee);
    }

    @Override
    @Transactional
    public void denyLeave(Leave leave) {
        leaveService.updateLeave(leave);
    }

    @Override
    public List<Leave> findAll() {
        return leaveService.findAllLeaves();
    }

    @Override
    public void save(Leave leave) {
        leaveService.addLeave(leave);
    }

    @Override
    public Leave update(Leave leave) {
        return leaveService.updateLeave(leave);
    }

    @Override
    public void deleteById(UUID id) {
        leaveService.deleteLeaveById(id);
    }

    @Override
    public List<LeaveStatus> listLeaveStatus() {
        return leaveService.listLeaveStatus();
    }

    @Override
    public List<LeaveType> listLeaveTypes() {
        return leaveService.listLeaveTypes();
    }

    @Override
    public List<Leave> findLeavesOfSubordinates(UUID id) {
        return null;
    }

    @Override
    public List<Leave> findByEmployeeId(UUID id) {
        return leaveService.findLeaveByEmployeeId(id);
    }

    @Override
    public List<Leave> findAllSubordinatesLeaves(Employee employee) {
        List<Employee> subordinates = employeeHierarchyService.findAllSubordinates(employee);
        return leaveService.findAllSubordinatesLeaves(subordinates);
    }
}
