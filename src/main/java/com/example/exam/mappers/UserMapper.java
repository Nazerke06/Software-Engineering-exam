package com.example.exam.mappers;

import com.example.exam.entities.User;
import com.example.exam.dto.userdto.UserRequestDTO;
import com.example.exam.dto.userdto.UserResponseDTO;
import org.mapstruct.Mapper;
import com.example.exam.entities.Role;
import org.mapstruct.Mapping;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "roles", qualifiedByName = "stringToRole")
    @Mapping(target = "password", ignore = true) // хэшируем в сервисе
    @Mapping(target = "id", ignore = true)
    User toEntity(UserRequestDTO dto);

    @Mapping(target = "roles", qualifiedByName = "roleToString")
    UserResponseDTO toDTO(User user);

    @Named("stringToRole")
    default Set<Role> stringToRole(Set<String> roleNames) {
        if (roleNames == null) return null;
        return roleNames.stream()
                .map(Role::valueOf)
                .collect(Collectors.toSet());
    }

    @Named("roleToString")
    default Set<String> roleToString(Set<Role> roles) {
        if (roles == null) return null;
        return roles.stream()
                .map(Role::name)
                .collect(Collectors.toSet());
    }
}