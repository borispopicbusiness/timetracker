package com.semiramide.timetracker.adapters.persistence.dto;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "leave")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaveDtoDB {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "employee_id")
  private UUID employeeId;

  @ManyToOne
  @JoinColumn(name = "employee_id", insertable = false, updatable = false)
  private EmployeeDtoDB employee;

  @Column(name = "start_time")
  private LocalDate startTime;

  @Column(name = "end_time")
  private LocalDate endTime;

  @Column(name = "description")
  private String description;

  @Column(name = "leave_type")
  private String leaveType;

  @Column(name = "leave_status")
  private String leaveStatus;

  @Column(name = "request_date")
  private LocalDate requestDate;

  @Column(name = "response_comment")
  private String responseComment;

  @Column(name = "responder_id")
  private UUID responderId;

  @ManyToOne
  @JoinColumn(name = "responder_id", insertable = false, updatable = false)
  private EmployeeDtoDB responderEmployee;
}
