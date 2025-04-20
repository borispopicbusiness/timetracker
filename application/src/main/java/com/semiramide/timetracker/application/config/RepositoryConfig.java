package com.semiramide.timetracker.application.config;

import com.semiramide.timetracker.adapters.persistence.repository.*;
import com.semiramide.timetracker.adapters.persistence.repository.jpa.*;
import com.semiramide.timetracker.adapters.persistence.repository.pagingAndSorting.PagingAndSortingRepository;
import com.semiramide.timetracker.core.repository.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfig {
    @Bean
    LeaveRepository leaveRepository(LeaveRepositoryJpa leaveRepositoryJpa) {
        return LeaveRepositoryImpl.builder().leaveRepositoryJpa(leaveRepositoryJpa).build();
    }

    @Bean
    EmployeeRepository employeeRepository(EmployeeRepositoryJpa employeeRepositoryJpa) {
        return EmployeeRepositoryImpl.builder().employeeRepositoryJpa(employeeRepositoryJpa).build();
    }

    @Bean
    WorklogRepository worklogRepository(
            WorklogRepositoryJpa worklogRepositoryJpa,
            PagingAndSortingRepository pagingAndSortingRepository) {
        return WorklogRepositoryImpl.builder()
                .worklogRepositoryJpa(worklogRepositoryJpa)
                .pagingAndSortingRepository(pagingAndSortingRepository)
                .build();
    }

    @Bean
    EmployeeHierarchyRepository employeeHierarchyRepository(
            EmployeeHierarchyRepositoryJpa employeeHierarchyRepositoryJpa) {
        return EmployeeHierarchyRepositoryImpl.builder()
                .employeeHierarchyRepositoryJpa(employeeHierarchyRepositoryJpa)
                .build();
    }

    @Bean
    ProjectRepository projectRepository(ProjectRepositoryJpa projectRepositoryJpa) {
        return ProjectRepositoryImpl.builder().projectRepositoryJpa(projectRepositoryJpa).build();
    }

    @Bean
    ProjectEmployeeRepository projectEmployeeRepository(
            ProjectEmployeeRepositoryJpa projectEmployeeRepositoryJpa) {
        return ProjectEmployeeRepositoryImpl.builder()
                .projectEmployeeRepositoryJpa(projectEmployeeRepositoryJpa)
                .build();
    }
}
