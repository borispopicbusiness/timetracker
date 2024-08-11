package com.semiramide.timetracker.adapters.persistence.repository.jpa;

import com.semiramide.timetracker.adapters.persistence.dto.EmployeeDtoDB;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepositoryJpa extends JpaRepository<EmployeeDtoDB, UUID> {

  EmployeeDtoDB findByEmail(String email);

  Optional<EmployeeDtoDB> findByPrincipalId(String principalId);

  List<EmployeeDtoDB> findAllByIdIn(List<UUID> uuids);
}
