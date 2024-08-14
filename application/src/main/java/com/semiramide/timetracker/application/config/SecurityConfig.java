package com.semiramide.timetracker.application.config;

import com.semiramide.timetracker.adapters.security.keycloak.KeycloakJwtRolesConverter;
import com.semiramide.timetracker.adapters.security.keycloak.KeycloakSecurityProperties;
import com.semiramide.timetracker.adapters.security.keycloak.KeycloakSecurityProvider;
import com.semiramide.timetracker.core.security.SecurityProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorityAuthorizationManager;
import org.springframework.security.authorization.AuthorizationManagers;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(
      HttpSecurity http, KeycloakJwtRolesConverter jwtRolesConverter) throws Exception {
    http.authorizeHttpRequests(
        auth ->
            auth.requestMatchers(
                    HttpMethod.POST,
                    "/employee",
                    "/project",
                    "/assign-subordinate",
                    "/project-employees")
                .access(
                    AuthorizationManagers.allOf(
                        AuthorityAuthorizationManager.hasAuthority("SCOPE_all"),
                        AuthorityAuthorizationManager.hasAnyRole("ADMIN")))
                .requestMatchers(
                    HttpMethod.DELETE, "/employee/**", "/project/**", "/project-employees/**")
                .access(
                    AuthorizationManagers.allOf(
                        AuthorityAuthorizationManager.hasAuthority("SCOPE_all"),
                        AuthorityAuthorizationManager.hasAnyRole("ADMIN")))
                .requestMatchers(HttpMethod.PUT, "/employee/me/**")
                .access(
                    AuthorizationManagers.allOf(
                        AuthorityAuthorizationManager.hasAuthority("SCOPE_all"),
                        AuthorityAuthorizationManager.hasAnyRole("EMPLOYEE", "ADMIN")))
                .requestMatchers(HttpMethod.PUT, "/project/**", "/employee/**")
                .access(
                    AuthorizationManagers.allOf(
                        AuthorityAuthorizationManager.hasAuthority("SCOPE_all"),
                        AuthorityAuthorizationManager.hasAnyRole("ADMIN")))
                .requestMatchers("/admin/**")
                .access(
                    AuthorizationManagers.allOf(
                        AuthorityAuthorizationManager.hasAuthority("SCOPE_all"),
                        AuthorityAuthorizationManager.hasAnyRole("ADMIN")))
                .requestMatchers("/test/**")
                .permitAll()
                .requestMatchers("/**")
                .access(
                    AuthorizationManagers.allOf(
                        AuthorityAuthorizationManager.hasAnyAuthority("SCOPE_all"),
                        AuthorityAuthorizationManager.hasAnyRole("EMPLOYEE", "ADMIN"))));
    http.oauth2ResourceServer().jwt().jwtAuthenticationConverter(jwtRolesConverter);
    return http.build();
  }

  @Bean
  SecurityProvider securityProvider(KeycloakSecurityProperties props) {
    return KeycloakSecurityProvider.builder().properties(props).build();
  }

  @Bean
  KeycloakJwtRolesConverter jwtRolesConverter() {
    return new KeycloakJwtRolesConverter();
  }
}
