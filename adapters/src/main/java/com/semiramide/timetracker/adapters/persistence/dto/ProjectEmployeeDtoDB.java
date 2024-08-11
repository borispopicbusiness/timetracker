package com.semiramide.timetracker.adapters.persistence.dto;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "project_employees")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectEmployeeDtoDB {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "projects_id")
  private UUID projectId;

  @Column(name = "employees_id")
  private UUID employeesId;
}
