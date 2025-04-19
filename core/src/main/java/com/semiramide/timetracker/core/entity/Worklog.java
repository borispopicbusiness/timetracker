package com.semiramide.timetracker.core.entity;

import com.semiramide.timetracker.core.entity.enums.WorklogType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Worklog {
    private UUID id;
    private UUID employeeId;
    private String taskName;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Double totalTime;
    private LocalDate creationDate;
    private UUID projectId;
    private WorklogType type;
}
