package com.semiramide.timetracker.gateway;

import com.semiramide.timetracker.gateway.properties.GatewayProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
//@ConfigurationPropertiesScan
@EnableConfigurationProperties(GatewayProperties.class)
public class Gateway {

  public static void main(String[] args) {
    SpringApplication.run(Gateway.class, args);
  }
}
