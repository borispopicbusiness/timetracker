package com.semiramide.timetracker.adapters.persistence.repository.jpa;

import com.semiramide.timetracker.adapters.persistence.dto.EmployeeHierarchyEntryDtoDB;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeHierarchyRepositoryJpa
        extends JpaRepository<EmployeeHierarchyEntryDtoDB, UUID> {

    // TODO: Investigate behavior of @Query annotation without native = true
    List<EmployeeHierarchyEntryDtoDB> findByParentIdOrChildId(UUID parentId, UUID childId);

    void deleteByParentIdAndChildId(UUID parentId, UUID childId);

    void deleteByParentIdOrChildId(UUID parentId, UUID childId);
}
