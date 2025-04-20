package com.semiramide.timetracker.adapters.persistence.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.semiramide.timetracker.adapters.api.dto.LeaveDtoAPI;
import com.semiramide.timetracker.adapters.api.mapper.LeaveMapperAPI;
import com.semiramide.timetracker.core.entity.Leave;
import com.semiramide.timetracker.core.entity.enums.LeaveStatus;
import com.semiramide.timetracker.core.entity.enums.LeaveType;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LeaveMapperAPITest {
    @Test
    @DisplayName(value = "Single Leave to LeaveDtoAPI")
    void should_MapWithoutErrors_SingleLeaveToLeaveDtoAPI() {
        // given
        UUID id = UUID.randomUUID();
        UUID employeeId = UUID.randomUUID();
        UUID responderId = UUID.randomUUID();
        LocalDate now = LocalDate.now();
        String description = "Some description";
        String responseComment = "The guy is really sick!";
        Leave leave =
                Leave.builder()
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

        LeaveDtoAPI expectedLeaveDtoAPI =
                LeaveDtoAPI.builder()
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
        LeaveDtoAPI actualLeaveDtoAPI = LeaveMapperAPI.INSTANCE.leaveToLeaveDtoAPI(leave);

        // then
        assertEquals(expectedLeaveDtoAPI, actualLeaveDtoAPI);
    }

    @Test
    @DisplayName(value = "List of Leave to List of LeaveDtoAPI")
    void should_MapWithoutErrors_LeaveListToLeaveDtoAPIList() {
        // given
        UUID id = UUID.randomUUID();
        UUID employeeId = UUID.randomUUID();
        UUID responderId = UUID.randomUUID();
        LocalDate now = LocalDate.now();
        String description = "Some description";
        String responseComment = "The guy is really sick!";

        Leave leave =
                Leave.builder()
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

        LeaveDtoAPI expectedLeaveDtoAPI =
                LeaveDtoAPI.builder()
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
        List<LeaveDtoAPI> expectedLeaveDtoAPIList = List.of(expectedLeaveDtoAPI);

        // when
        List<LeaveDtoAPI> actualLeaveDtoAPIList =
                LeaveMapperAPI.INSTANCE.leaveListToLeaveDtoAPIList(leaveList);

        // then
        assertEquals(expectedLeaveDtoAPIList, actualLeaveDtoAPIList);
    }

    @Test
    @DisplayName(value = "Single LeaveDtoAPI to Leave")
    void should_MapWithoutErrors_SingleLeaveDtoAPIToLeave() {
        // given
        UUID id = UUID.randomUUID();
        UUID employeeId = UUID.randomUUID();
        UUID responderId = UUID.randomUUID();
        LocalDate now = LocalDate.now();
        String description = "Some description";
        String responseComment = "The guy is really sick!";

        LeaveDtoAPI leaveDtoAPI =
                LeaveDtoAPI.builder()
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
        Leave actualLeave = LeaveMapperAPI.INSTANCE.leaveDtoAPIToLeave(leaveDtoAPI);

        // then
        assertEquals(expectedLeave, actualLeave);
    }

    @Test
    @DisplayName(value = "List of LeaveDtoAPI to List of Leave")
    void should_MapWithoutErrors_LeaveDtoAPIListToLeaveList() {
        // given
        UUID id = UUID.randomUUID();
        UUID employeeId = UUID.randomUUID();
        UUID responderId = UUID.randomUUID();
        LocalDate now = LocalDate.now();
        String description = "Some description";
        String responseComment = "The guy is really sick!";

        LeaveDtoAPI leaveDtoAPI =
                LeaveDtoAPI.builder()
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
        List<LeaveDtoAPI> LeaveDtoAPIList = List.of(leaveDtoAPI);

        Leave expectedLeave =
                Leave.builder()
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
        List<Leave> actualLeaveList =
                LeaveMapperAPI.INSTANCE.leaveDtoAPIListToLeaveList(LeaveDtoAPIList);

        // then
        assertEquals(expectedLeaveList, actualLeaveList);
    }
}
