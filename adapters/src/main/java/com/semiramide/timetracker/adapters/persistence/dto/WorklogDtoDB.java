package com.semiramide.timetracker.adapters.persistence.dto;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "worklog")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorklogDtoDB {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "employee_id")
    private UUID employeeId;

    @ManyToOne
    @JoinColumn(name = "employee_id", insertable = false, updatable = false)
    private EmployeeDtoDB employee;

    @Column(name = "project_id")
    private UUID projectId;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false, insertable = false, updatable = false)
    private ProjectDtoDB project;

    @Column(name = "task_name")
    private String taskName;

    @Column(name = "description")
    private String description;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "total_time")
    private Double totalTime;

    @Column(name = "creation_date")
    private LocalDate creationDate;

    @Column(name = "type")
    private String type;
}
