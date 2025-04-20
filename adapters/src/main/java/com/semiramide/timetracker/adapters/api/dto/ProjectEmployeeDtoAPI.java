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
public class ProjectEmployeeDtoAPI {

    private UUID id;

    private UUID projectId;

    private UUID employeesId;
}
