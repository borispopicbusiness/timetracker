package com.semiramide.timetracker.adapters.persistence.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.semiramide.timetracker.adapters.persistence.dto.LeaveDtoDB;
import com.semiramide.timetracker.core.entity.Leave;
import com.semiramide.timetracker.core.entity.enums.LeaveStatus;
import com.semiramide.timetracker.core.entity.enums.LeaveType;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LeaveMapperDBTest {

  @Test
  @DisplayName(value = "Single Leave to LeaveDtoDB")
  void should_MapWithoutErrors_SingleLeaveToLeaveDtoDB() {
    // given
    UUID id = UUID.randomUUID();
    UUID employeeId = UUID.randomUUID();
    UUID responderId = UUID.randomUUID();
    LocalDate now = LocalDate.now();
    String description = "Some description";
    String responseComment = "The guy is really sick!";
    Leave leave =
        Leave.builder()
            .id(id)
            .employeeId(employeeId)
            .startTime(now)
            .endTime(now)
            .description(description)
            .leaveType(LeaveType.SICK_LEAVE)
            .leaveStatus(LeaveStatus.APPROVED)
            .requestDate(now)
            .responseComment(responseComment)
            .responderId(responderId)
            .build();

    LeaveDtoDB expectedLeaveDtoDB =
        LeaveDtoDB.builder()
            .id(id)
            .employeeId(employeeId)
            .startTime(now)
            .endTime(now)
            .description(description)
            .leaveType("SICK_LEAVE")
            .leaveStatus("APPROVED")
            .requestDate(now)
            .responseComment(responseComment)
            .responderId(responderId)
            .build();

    // when
    LeaveDtoDB actualLeaveDtoDB = LeaveMapperDB.INSTANCE.leaveToLeaveDtoDB(leave);

    // then
    assertEquals(expectedLeaveDtoDB, actualLeaveDtoDB);
  }

  @Test
  @DisplayName(value = "List of Leave to List of LeaveDtoDB")
  void should_MapWithoutErrors_LeaveListToLeaveDtoDBList() {
    // given
    UUID id = UUID.randomUUID();
    UUID employeeId = UUID.randomUUID();
    UUID responderId = UUID.randomUUID();
    LocalDate now = LocalDate.now();
    String description = "Some description";
    String responseComment = "The guy is really sick!";

    Leave leave =
        Leave.builder()
            .id(id)
            .employeeId(employeeId)
            .startTime(now)
            .endTime(now)
            .description(description)
            .leaveType(LeaveType.SICK_LEAVE)
            .leaveStatus(LeaveStatus.APPROVED)
            .requestDate(now)
            .responseComment(responseComment)
            .responderId(responderId)
            .build();
    List<Leave> leaveList = List.of(leave);

    LeaveDtoDB expectedLeaveDtoDB =
        LeaveDtoDB.builder()
            .id(id)
            .employeeId(employeeId)
            .startTime(now)
            .endTime(now)
            .description(description)
            .leaveType("SICK_LEAVE")
            .leaveStatus("APPROVED")
            .requestDate(now)
            .responseComment(responseComment)
            .responderId(responderId)
            .build();
    List<LeaveDtoDB> expectedLeaveDtoDBList = List.of(expectedLeaveDtoDB);

    // when
    List<LeaveDtoDB> actualLeaveDtoDBList =
        LeaveMapperDB.INSTANCE.leaveListToLeaveDtoDBList(leaveList);

    // then
    assertEquals(expectedLeaveDtoDBList, actualLeaveDtoDBList);
  }

  @Test
  @DisplayName(value = "Single LeaveDtoDB to Leave")
  void should_MapWithoutErrors_SingleLeaveDtoDBToLeave() {
    // given
    UUID id = UUID.randomUUID();
    UUID employeeId = UUID.randomUUID();
    UUID responderId = UUID.randomUUID();
    LocalDate now = LocalDate.now();
    String description = "Some description";
    String responseComment = "The guy is really sick!";

    LeaveDtoDB leaveDtoDB =
        LeaveDtoDB.builder()
            .id(id)
            .employeeId(employeeId)
            .startTime(now)
            .endTime(now)
            .description(description)
            .leaveType("SICK_LEAVE")
            .leaveStatus("APPROVED")
            .requestDate(now)
            .responseComment(responseComment)
            .responderId(responderId)
            .build();

    Leave expectedLeave =
        Leave.builder()
            .id(id)
            .employeeId(employeeId)
            .startTime(now)
            .endTime(now)
            .description(description)
            .leaveType(LeaveType.SICK_LEAVE)
            .leaveStatus(LeaveStatus.APPROVED)
            .requestDate(now)
            .responseComment(responseComment)
            .responderId(responderId)
            .build();

    // when
    Leave actualLeave = LeaveMapperDB.INSTANCE.leaveDtoDBToLeave(leaveDtoDB);

    // then
    assertEquals(expectedLeave, actualLeave);
  }

  @Test
  @DisplayName(value = "List of LeaveDtoDB to List of Leave")
  void should_MapWithoutErrors_LeaveDtoDBListToLeaveList() {
    // given
    UUID id = UUID.randomUUID();
    UUID employeeId = UUID.randomUUID();
    UUID responderId = UUID.randomUUID();
    LocalDate now = LocalDate.now();
    String description = "Some description";
    String responseComment = "The guy is really sick!";

    LeaveDtoDB leaveDtoDB =
        LeaveDtoDB.builder()
            .id(id)
            .employeeId(employeeId)
            .startTime(now)
            .endTime(now)
            .description(description)
            .leaveType("SICK_LEAVE")
            .leaveStatus("APPROVED")
            .requestDate(now)
            .responseComment(responseComment)
            .responderId(responderId)
            .build();
    List<LeaveDtoDB> leaveDtoDBList = List.of(leaveDtoDB);

    Leave expectedLeave =
        Leave.builder()
            .id(id)
            .employeeId(employeeId)
            .startTime(now)
            .endTime(now)
            .description(description)
            .leaveType(LeaveType.SICK_LEAVE)
            .leaveStatus(LeaveStatus.APPROVED)
            .requestDate(now)
            .responseComment(responseComment)
            .responderId(responderId)
            .build();
    List<Leave> expectedLeaveList = List.of(expectedLeave);

    // when
    List<Leave> actualLeaveList = LeaveMapperDB.INSTANCE.leaveDtoDBListToLeaveList(leaveDtoDBList);

    // then
    assertEquals(expectedLeaveList, actualLeaveList);
  }
}
