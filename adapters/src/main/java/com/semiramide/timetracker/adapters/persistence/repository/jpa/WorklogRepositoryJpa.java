package com.semiramide.timetracker.adapters.persistence.repository.jpa;

import com.semiramide.timetracker.adapters.persistence.dto.WorklogDtoDB;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface WorklogRepositoryJpa
        extends JpaRepository<WorklogDtoDB, UUID>, JpaSpecificationExecutor<WorklogDtoDB> {

    List<WorklogDtoDB> findByEmployeeId(UUID employeeId);

    List<WorklogDtoDB> findByEmployeeIdAndCreationDate(UUID employeeId, LocalDate creationDate);

    void deleteByEmployeeId(UUID employeeId);
}
