package com.semiramide.timetracker.adapters.persistence.repository;

import com.semiramide.timetracker.adapters.persistence.dto.EmployeeHierarchyEntryDtoDB;
import com.semiramide.timetracker.adapters.persistence.mapper.EmployeeHierarchyEntryMapperDB;
import com.semiramide.timetracker.adapters.persistence.repository.jpa.EmployeeHierarchyRepositoryJpa;
import com.semiramide.timetracker.core.entity.EmployeeHierarchyEntry;
import com.semiramide.timetracker.core.repository.EmployeeHierarchyRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.Builder;

@Builder
public class EmployeeHierarchyRepositoryImpl implements EmployeeHierarchyRepository {

  private EmployeeHierarchyRepositoryJpa employeeHierarchyRepositoryJpa;

  @Override
  public Optional<EmployeeHierarchyEntry> saveEmployeeHierarchyEntry(
      EmployeeHierarchyEntry employeeHierarchyEntry) {
    EmployeeHierarchyEntryDtoDB entry =
        EmployeeHierarchyEntryMapperDB.INSTANCE.employeeHierarchyEntryToEmployeeHierarchyEntryDtoDB(
            employeeHierarchyEntry);
    EmployeeHierarchyEntryDtoDB createdEntry = employeeHierarchyRepositoryJpa.save(entry);
    return Optional.of(
        EmployeeHierarchyEntryMapperDB.INSTANCE.employeeHierarchyEntryDtoDBToEmployeeHierarchyEntry(
            createdEntry));
  }

  @Override
  public List<EmployeeHierarchyEntry> findAllEmployeeHierarchyEntries() {
    return EmployeeHierarchyEntryMapperDB.INSTANCE
        .employeeHierarchyEntryDtoDBListToEmployeeHierarchyEntryList(
            employeeHierarchyRepositoryJpa.findAll());
  }

  @Override
  public void deleteByParentIdAndChildId(UUID parentId, UUID childId) {
    employeeHierarchyRepositoryJpa.deleteByParentIdAndChildId(parentId, childId);
  }

  @Override
  public void deleteEmployeeHierarchyEntry(EmployeeHierarchyEntry entry) {
    employeeHierarchyRepositoryJpa.delete(
        EmployeeHierarchyEntryMapperDB.INSTANCE.employeeHierarchyEntryToEmployeeHierarchyEntryDtoDB(
            entry));
  }

  @Override
  public void deleteEmployeeHierarchyEntriesByEmployeeId(UUID employeeId) {
    employeeHierarchyRepositoryJpa.deleteByParentIdOrChildId(employeeId, employeeId);
  }

  @Override
  public List<EmployeeHierarchyEntry> findAllEmployeeHierarchyEntriesByEmployeeId(UUID employeeId) {
    // Find all entries where employee with given ID is either parent or child.
    List<EmployeeHierarchyEntryDtoDB> list =
        employeeHierarchyRepositoryJpa.findByParentIdOrChildId(employeeId, employeeId);
    return EmployeeHierarchyEntryMapperDB.INSTANCE
        .employeeHierarchyEntryDtoDBListToEmployeeHierarchyEntryList(list);
  }
}
