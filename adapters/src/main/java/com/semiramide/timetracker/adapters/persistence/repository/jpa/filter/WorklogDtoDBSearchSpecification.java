package com.semiramide.timetracker.adapters.persistence.repository.jpa.filter;

import com.semiramide.timetracker.adapters.persistence.dto.WorklogDtoDB;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

public class WorklogDtoDBSearchSpecification {

    public static Specification<WorklogDtoDB> getSpecification(String filedName, String[] values) {
        switch ( filedName ) {
            case "employeeId": {
                return Specification.anyOf(employeeIdSpecification(filedName, values));
            }
            case "projectId": {
                return Specification.anyOf(projectIdSpecification(filedName, values));
            }
            case "dateRange": {
                return Specification.allOf(dateRangeSpecification(filedName, values));
            }
            case "type": {
                return Specification.anyOf(typeSpecification(filedName, values));
            }
            default: {
                return null;
            }
        }
    }

    private static List<Specification<WorklogDtoDB>> employeeIdSpecification(
            String fieldName, String[] values) {
        List<Specification<WorklogDtoDB>> specifications = new ArrayList<>();
        for ( String value : values ) {
            specifications.add((r, q, b) -> (b.equal(r.get(fieldName), UUID.fromString(value))));
        }
        return specifications;
    }

    private static List<Specification<WorklogDtoDB>> projectIdSpecification(
            String fieldName, String[] values) {
        List<Specification<WorklogDtoDB>> specifications = new ArrayList<>();
        for ( String value : values ) {
            specifications.add((r, q, b) -> (b.equal(r.get(fieldName), UUID.fromString(value))));
        }
        return specifications;
    }

    private static List<Specification<WorklogDtoDB>> dateRangeSpecification(
            String fieldName, String[] values) {
        List<Specification<WorklogDtoDB>> specifications = new ArrayList<>();
        specifications.add(
                (r, q, b) ->
                        (b.greaterThanOrEqualTo(
                                r.get("creationDate").as(LocalDate.class), LocalDate.parse(values[0]))));
        specifications.add(
                (r, q, b) ->
                        (b.lessThanOrEqualTo(
                                r.get("creationDate").as(LocalDate.class), LocalDate.parse(values[1]))));
        return specifications;
    }

    private static List<Specification<WorklogDtoDB>> typeSpecification(
            String fieldName, String[] values) {
        List<Specification<WorklogDtoDB>> specifications = new ArrayList<>();
        for ( String value : values ) {
            specifications.add((r, q, b) -> (b.equal(r.get(fieldName), value)));
        }
        return specifications;
    }
}
