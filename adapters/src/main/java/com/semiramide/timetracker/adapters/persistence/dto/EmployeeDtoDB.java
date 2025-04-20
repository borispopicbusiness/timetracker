package com.semiramide.timetracker.adapters.persistence.dto;

import jakarta.persistence.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "employee")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDtoDB {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "principal_id")
    private String principalId;

    @Column(name = "free_days_left")
    private Integer freeDaysLeft;

    @OneToMany(mappedBy = "employee")
    private List<LeaveDtoDB> leaves;

    @OneToMany(mappedBy = "responderEmployee")
    private List<LeaveDtoDB> respondedLeaves;

    @OneToMany(mappedBy = "employee")
    private List<WorklogDtoDB> worklogs;

    @ManyToMany(mappedBy = "employees")
    private Set<ProjectDtoDB> projects;
}
