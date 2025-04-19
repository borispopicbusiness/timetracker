package com.semiramide.timetracker.core.usecase;

import com.semiramide.timetracker.core.entity.Employee;
import com.semiramide.timetracker.core.entity.Leave;
import com.semiramide.timetracker.core.entity.enums.LeaveStatus;
import com.semiramide.timetracker.core.entity.enums.LeaveType;
import com.semiramide.timetracker.core.exception.EmployeeNotFoundException;
import com.semiramide.timetracker.core.exception.FreeDaysLeftException;

import java.util.List;
import java.util.UUID;

public interface LeaveUseCase {
    Leave requestLeave(Leave leave) throws EmployeeNotFoundException, FreeDaysLeftException;

    void approveLeave(Leave leave) throws EmployeeNotFoundException, FreeDaysLeftException;

    void denyLeave(Leave leave);

    List<Leave> findAll();

    void save(Leave leave);

    Leave update(Leave leave);

    void deleteById(UUID id);

    List<LeaveStatus> listLeaveStatus();

    List<LeaveType> listLeaveTypes();

    List<Leave> findLeavesOfSubordinates(UUID id);

    List<Leave> findByEmployeeId(UUID id);

    List<Leave> findAllSubordinatesLeaves(Employee employee);
}
