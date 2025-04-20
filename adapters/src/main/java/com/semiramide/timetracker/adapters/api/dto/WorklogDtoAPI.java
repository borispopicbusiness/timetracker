package com.semiramide.timetracker.adapters.api.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorklogDtoAPI {

    private UUID id;

    private UUID employeeId;

    private String taskName;

    private String description;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private LocalDate creationDate;

    private Double totalTime;

    private UUID projectId;

    private String type;
}
