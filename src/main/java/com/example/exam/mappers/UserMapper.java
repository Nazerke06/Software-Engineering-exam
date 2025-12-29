package com.example.exam.mappers;

import com.example.exam.dto.userdto.UserRequestDTO;
import com.example.exam.dto.userdto.UserResponseDTO;
import com.example.exam.entities.Role;
import com.example.exam.entities.User;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "role", source = "roles", qualifiedByName = "stringToRole")
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "id", ignore = true)
    User toEntity(UserRequestDTO dto);

    @Mapping(target = "roles", source = "role", qualifiedByName = "roleToSet")
    UserResponseDTO toDTO(User user);

    // Set<String> → Role (берем первую роль)
    @Named("stringToRole")
    default Role stringToRole(Set<String> roles) {
        if (roles == null || roles.isEmpty()) return null;
        return Role.valueOf(roles.iterator().next());
    }

    // Role → Set<String>
    @Named("roleToSet")
    default Set<String> roleToSet(Role role) {
        if (role == null) return null;
        return Set.of(role.name());
    }
}

