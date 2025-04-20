package com.semiramide.timetracker.adapters.persistence.dto;

import jakarta.persistence.*;

import java.util.Set;
import java.util.UUID;

import lombok.Data;

@Entity(name = "project")
@Data
public class ProjectDtoDB {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToMany
    private Set<EmployeeDtoDB> employees;

    @OneToMany(mappedBy = "project")
    private Set<WorklogDtoDB> worklogs;
}
