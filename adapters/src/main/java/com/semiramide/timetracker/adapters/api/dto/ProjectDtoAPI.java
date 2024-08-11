package com.semiramide.timetracker.adapters.api.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDtoAPI {

  private UUID id;

  private String name;

  private String description;
}
