package com.semiramide.timetracker.adapters.api.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDtoAPI {

  private UUID id;

  private String firstName;

  private String lastName;

  private String email;

  private String password;

  private Integer freeDaysLeft;
}
