package com.semiramide.timetracker.adapters.security.keycloak.mapper;

import com.semiramide.timetracker.adapters.security.keycloak.dto.CredentialsDto;
import com.semiramide.timetracker.adapters.security.keycloak.dto.PrincipalDto;
import com.semiramide.timetracker.core.entity.Employee;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "Spring")
public interface PrincipalMapper {
    PrincipalMapper INSTANCE = Mappers.getMapper(PrincipalMapper.class);

    @Named(value = "passwordToCredentialsDtoList")
    static List<CredentialsDto> passwordToCredentialsDtoList(String password) {
        return List.of(CredentialsDto.builder().value(password).temporary(false).build());
    }

    @Mapping(target = "enabled", constant = "true")
    @Mapping(source = "email", target = "username")
    @Mapping(
            source = "password",
            target = "credentials",
            qualifiedByName = "passwordToCredentialsDtoList")
    PrincipalDto employeeToPrincipal(Employee employee);
}
