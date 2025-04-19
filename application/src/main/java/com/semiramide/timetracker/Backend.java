package com.semiramide.timetracker;

import com.semiramide.timetracker.adapters.security.keycloak.KeycloakSecurityProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
@EnableConfigurationProperties(KeycloakSecurityProperties.class)
public class Backend {

  public static void main(String[] args) {
    SpringApplication.run(Backend.class, args);
  }
}
