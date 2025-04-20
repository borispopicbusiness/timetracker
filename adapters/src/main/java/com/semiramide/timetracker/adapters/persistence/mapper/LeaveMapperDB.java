package com.semiramide.timetracker.adapters.persistence.mapper;

import com.semiramide.timetracker.adapters.persistence.dto.LeaveDtoDB;
import com.semiramide.timetracker.core.entity.Leave;
import com.semiramide.timetracker.core.entity.enums.LeaveStatus;
import com.semiramide.timetracker.core.entity.enums.LeaveType;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface LeaveMapperDB {

    LeaveMapperDB INSTANCE = Mappers.getMapper(LeaveMapperDB.class);

    @Named(value = "leaveStatusAsStringToLeaveStatus")
    static LeaveStatus leaveStatusAsStringToLeaveStatus(String leaveStatusAsString) {
        return LeaveStatus.fromName(leaveStatusAsString);
    }

    @Named(value = "leaveTypeAsStringToLeaveType")
    static LeaveType leaveTypeAsStringToLeaveType(String leaveTypeAsString) {
        return LeaveType.fromName(leaveTypeAsString);
    }

    @Named(value = "leaveStatusToLeaveStatusAsString")
    static String leaveStatusToLeaveStatusAsString(LeaveStatus leaveStatus) {
        return leaveStatus.getName();
    }

    @Named(value = "leaveTypeToLeaveTypeAsString")
    static String leaveTypeToLeaveTypeAsString(LeaveType leaveType) {
        return leaveType.getName();
    }

    @Mapping(
            source = "leaveStatus",
            target = "leaveStatus",
            qualifiedByName = "leaveStatusAsStringToLeaveStatus")
    @Mapping(
            source = "leaveType",
            target = "leaveType",
            qualifiedByName = "leaveTypeAsStringToLeaveType")
    Leave leaveDtoDBToLeave(LeaveDtoDB leaveDtoDB);

    @Mapping(
            source = "leaveStatus",
            target = "leaveStatus",
            qualifiedByName = "leaveStatusToLeaveStatusAsString")
    @Mapping(
            source = "leaveType",
            target = "leaveType",
            qualifiedByName = "leaveTypeToLeaveTypeAsString")
    LeaveDtoDB leaveToLeaveDtoDB(Leave leave);

    List<LeaveDtoDB> leaveListToLeaveDtoDBList(List<Leave> leaveList);

    List<Leave> leaveDtoDBListToLeaveList(List<LeaveDtoDB> leaveDtoDBList);
}
