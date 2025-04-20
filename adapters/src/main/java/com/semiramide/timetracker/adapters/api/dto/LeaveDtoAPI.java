package com.semiramide.timetracker.adapters.api.dto;

import java.time.LocalDate;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaveDtoAPI {

    private UUID id;

    private UUID employeeId;

    private LocalDate startTime;

    private LocalDate endTime;

    private String description;

    private String leaveType;

    private String leaveStatus;

    private LocalDate requestDate;

    private String responseComment;

    private UUID responderId;
}
