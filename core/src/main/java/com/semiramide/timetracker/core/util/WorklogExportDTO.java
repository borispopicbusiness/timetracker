package com.semiramide.timetracker.core.util;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorklogExportDTO {
    private String employee;
    private String project;
    private String type;
    private String task;
    private String description;
    private double time;
    private LocalDate date;
}
