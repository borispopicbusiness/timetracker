package com.semiramide.timetracker.adapters.security.keycloak.dto;

import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PrincipalDto {

  private String username;
  private String firstName;
  private String lastName;
  private String email;
  private List<CredentialsDto> credentials;
  private boolean enabled;
  private Map<String, List<String>> clientRoles;
}
