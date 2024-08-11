package com.semiramide.timetracker.core.repository;

import com.semiramide.timetracker.core.entity.EmployeeHierarchyEntry;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmployeeHierarchyRepository {

  Optional<EmployeeHierarchyEntry> saveEmployeeHierarchyEntry(
      EmployeeHierarchyEntry employeeHierarchyEntry);

  List<EmployeeHierarchyEntry> findAllEmployeeHierarchyEntries();

  void deleteByParentIdAndChildId(UUID parentId, UUID childId);

  void deleteEmployeeHierarchyEntry(EmployeeHierarchyEntry entry);

  void deleteEmployeeHierarchyEntriesByEmployeeId(UUID employeeId);

  List<EmployeeHierarchyEntry> findAllEmployeeHierarchyEntriesByEmployeeId(UUID employeeId);
}
