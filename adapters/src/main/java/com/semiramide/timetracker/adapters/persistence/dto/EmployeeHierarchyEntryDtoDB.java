package com.semiramide.timetracker.adapters.persistence.dto;

import jakarta.persistence.*;

import java.util.UUID;

import lombok.Data;

@Entity(name = "employee_hierarchy")
@Data
public class EmployeeHierarchyEntryDtoDB {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "parent_id")
    private UUID parentId;

    @Column(name = "child_id")
    private UUID childId;
}
