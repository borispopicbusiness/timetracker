package com.semiramide.timetracker.application.config;

import com.semiramide.timetracker.adapters.event.AppEventListenerImpl;
import com.semiramide.timetracker.adapters.event.AppEventPublisherImpl;
import com.semiramide.timetracker.core.event.AppEventListener;
import com.semiramide.timetracker.core.event.AppEventPublisher;
import com.semiramide.timetracker.core.hierarchy.EmployeeHierarchyGraph;
import com.semiramide.timetracker.core.hierarchy.impl.GuavaMutableGraph;
import com.semiramide.timetracker.core.init.ApplicationInitializer;
import com.semiramide.timetracker.core.security.SecurityProvider;
import com.semiramide.timetracker.core.service.EmployeeHierarchyService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MiscellaneousConfig {

  @Bean
  EmployeeHierarchyGraph employeeHierarchyGraph() {
    return new GuavaMutableGraph();
  }

  @Bean
  ApplicationInitializer initializingBean(
      EmployeeHierarchyService employeeHierarchyService, SecurityProvider securityProvider) {
    return ApplicationInitializer.builder()
        .employeeHierarchyService(employeeHierarchyService)
        .securityProvider(securityProvider)
        .build();
  }

  @Bean
  AppEventPublisher eventPublisher(ApplicationEventPublisher publisher) {
    return AppEventPublisherImpl.builder().publisher(publisher).build();
  }

  @Bean
  AppEventListener eventListener(EmployeeHierarchyService employeeHierarchyService) {
    return AppEventListenerImpl.builder()
        .employeeHierarchyService(employeeHierarchyService)
        .build();
  }
}
