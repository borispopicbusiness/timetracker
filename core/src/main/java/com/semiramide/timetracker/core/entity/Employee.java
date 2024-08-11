package com.semiramide.timetracker.core.entity;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {

  private UUID id;
  private String firstName;
  private String lastName;
  private String email;
  private String password;
  private String principalId;
  private Integer freeDaysLeft;
}
