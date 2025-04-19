package com.semiramide.timetracker.core.entity;

import com.semiramide.timetracker.core.entity.enums.LeaveStatus;
import com.semiramide.timetracker.core.entity.enums.LeaveType;

import java.time.LocalDate;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Leave {
    private UUID id;
    private UUID employeeId;
    private LocalDate startTime;
    private LocalDate endTime;
    private String description;
    private LeaveType leaveType;
    private LeaveStatus leaveStatus;
    private LocalDate requestDate;
    private String responseComment;
    private UUID responderId;
}
