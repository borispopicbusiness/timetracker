package com.semiramide.timetracker.core.service.impl;

import com.semiramide.timetracker.core.entity.Employee;
import com.semiramide.timetracker.core.entity.Leave;
import com.semiramide.timetracker.core.entity.enums.LeaveStatus;
import com.semiramide.timetracker.core.entity.enums.LeaveType;
import com.semiramide.timetracker.core.repository.LeaveRepository;
import com.semiramide.timetracker.core.service.LeaveService;
import com.google.common.collect.Sets;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Builder
@Slf4j
public class LeaveServiceImpl implements LeaveService {

  private final LeaveRepository leaveRepository;

  @Override
  public List<Leave> findAllLeaves() {
    return leaveRepository.findAllLeaves();
  }

  @Override
  public Leave addLeave(Leave leave) {
    return leaveRepository.saveLeave(leave);
  }

  @Override
  public Leave updateLeave(Leave leave) {
    return leaveRepository.saveLeave(leave);
  }

  @Override
  public Optional<Leave> findLeaveById(UUID id) {
    return leaveRepository.findLeaveById(id);
  }

  @Override
  public void deleteLeaveById(UUID id) {
    Optional<Leave> leave = leaveRepository.findLeaveById(id);
    if (leave.isPresent()) {
      leaveRepository.deleteLeave(leave.get());
    } else {
      log.error("There is no leave with id: " + id);
    }
  }

  @Override
  public boolean hasOverlaps(Leave leave) {
    List<Leave> l1 = leaveRepository.findByStartTimeBefore(leave.getEndTime().plusDays(1));
    List<Leave> l2 = leaveRepository.findByEndTimeAfter(leave.getStartTime().minusDays(1));
    return !Sets.intersection(new HashSet<>(l1), new HashSet<>(l2)).isEmpty();
  }

  @Override
  public boolean isStartBeforeOrSameAsEnd(Leave leave) {
    return leave.getStartTime().isBefore(leave.getEndTime())
        || leave.getStartTime().isEqual(leave.getEndTime());
  }

  @Override
  public boolean isStartInThePast(Leave leave) {
    return leave.getStartTime().isBefore(LocalDate.now());
  }

  @Override
  public void deleteAllLeaves() {
    leaveRepository.deleteAllLeaves();
  }

  @Override
  public void deleteByEmployeeId(UUID employeeId) {
    leaveRepository.deleteByEmployeeId(employeeId);
  }

  @Override
  public List<LeaveStatus> listLeaveStatus() {
    return List.of(LeaveStatus.values());
  }

  @Override
  public List<LeaveType> listLeaveTypes() {
    return List.of(LeaveType.values());
  }

  @Override
  public List<Leave> findLeaveByEmployeeId(UUID id) {
    return leaveRepository.findByEmployeeId(id);
  }

  @Override
  public List<Leave> findAllSubordinatesLeaves(List<Employee> subordinates) {
    return leaveRepository.findAllSubordinatesLeaves(subordinates);
  }
}
