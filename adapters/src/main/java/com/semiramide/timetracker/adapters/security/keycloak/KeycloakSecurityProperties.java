package com.semiramide.timetracker.adapters.security.keycloak;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
//@Configuration
@ConfigurationProperties(prefix = "tt.security.provider.keycloak")
public class KeycloakSecurityProperties {

  private String host;
  private String realm;
  private String username;
  private String password;
  private String adminClientName;
  private String frontendClientName;
  private String grantType;
  private String getClientIdUrl;
  private String getAvailableRolesUrl;
  private String createPrincipalUrl;
  private String updatePrincipalUrl;
  private String deletePrincipalUrl;
  private String assignClientRoleUrl;
  private String authorizeUrl;
  private String defaultAdminUsername;
  private String defaultAdminPassword;
  private String getPrincipalCountUrl;
}
