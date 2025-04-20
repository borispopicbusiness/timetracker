package com.semiramide.timetracker.adapters.persistence.repository;

import com.semiramide.timetracker.adapters.persistence.mapper.LeaveMapperDB;
import com.semiramide.timetracker.adapters.persistence.repository.jpa.LeaveRepositoryJpa;
import com.semiramide.timetracker.core.entity.Employee;
import com.semiramide.timetracker.core.entity.Leave;
import com.semiramide.timetracker.core.repository.LeaveRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import lombok.Builder;

@Builder
public class LeaveRepositoryImpl implements LeaveRepository {

    private final LeaveRepositoryJpa leaveRepositoryJpa;

    @Override
    public List<Leave> findAllLeaves() {
        return LeaveMapperDB.INSTANCE.leaveDtoDBListToLeaveList(leaveRepositoryJpa.findAll());
    }

    @Override
    public Leave saveLeave(Leave leave) {
        return LeaveMapperDB.INSTANCE.leaveDtoDBToLeave(
                leaveRepositoryJpa.save(LeaveMapperDB.INSTANCE.leaveToLeaveDtoDB(leave)));
    }

    @Override
    public Optional<Leave> findLeaveById(UUID id) {
        return Optional.ofNullable(
                LeaveMapperDB.INSTANCE.leaveDtoDBToLeave(leaveRepositoryJpa.findById(id).orElse(null)));
    }

    @Override
    public void deleteLeave(Leave leave) {
        leaveRepositoryJpa.delete(LeaveMapperDB.INSTANCE.leaveToLeaveDtoDB(leave));
    }

    @Override
    public void deleteAllLeaves() {
        leaveRepositoryJpa.deleteAll();
    }

    @Override
    public void deleteByEmployeeId(UUID employeeId) {
        leaveRepositoryJpa.deleteByEmployeeId(employeeId);
    }

    @Override
    public List<Leave> findByEmployeeId(UUID id) {
        return LeaveMapperDB.INSTANCE.leaveDtoDBListToLeaveList(
                leaveRepositoryJpa.findByEmployeeId(id));
    }

    @Override
    public List<Leave> findAllSubordinatesLeaves(List<Employee> subordinates) {
        List<Leave> leaves = new ArrayList<>();
        for ( Employee subordinate : subordinates ) {
            List<Leave> subordinateLeaves =
                    LeaveMapperDB.INSTANCE.leaveDtoDBListToLeaveList(
                            leaveRepositoryJpa.findByEmployeeId(subordinate.getId()));
            for ( Leave leave : subordinateLeaves ) {
                leaves.add(leave);
            }
        }
        return leaves;
    }

    @Override
    public List<Leave> findByStartTimeBefore(LocalDate endTime) {
        return LeaveMapperDB.INSTANCE.leaveDtoDBListToLeaveList(
                leaveRepositoryJpa.findByStartTimeBefore(endTime));
    }

    @Override
    public List<Leave> findByEndTimeAfter(LocalDate startTime) {
        return LeaveMapperDB.INSTANCE.leaveDtoDBListToLeaveList(
                leaveRepositoryJpa.findByEndTimeAfter(startTime));
    }
}
