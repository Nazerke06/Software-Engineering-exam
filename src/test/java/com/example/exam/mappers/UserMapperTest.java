package com.example.exam.mappers;

import com.example.exam.dto.userdto.UserRequestDTO;
import com.example.exam.dto.userdto.UserResponseDTO;
import com.example.exam.entities.Role;
import com.example.exam.entities.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private final UserMapper mapper = Mappers.getMapper(UserMapper.class);

    @Test
    void toEntity_shouldMapCorrectly_andIgnorePasswordAndId() {
        // Given
        UserRequestDTO dto = UserRequestDTO.builder()
                .username("doctor1")
                .password("rawPassword") // should be ignored
                .fullName("Dr. John Doe")
                .roles(Set.of("ADMIN", "DOCTOR"))
                .build();

        // When
        User user = mapper.toEntity(dto);

        // Then
        assertNull(user.getId());
        assertEquals("doctor1", user.getUsername());
        assertNull(user.getPassword()); // ignored
        assertEquals("Dr. John Doe", user.getFullName());
        assertEquals(Set.of(Role.ADMIN, Role.DOCTOR), user.getRoles());
        assertFalse(user.isEnabled()); // default value
    }

    @Test
    void toDTO_shouldMapCorrectly() {
        // Given
        User user = User.builder()
                .id(1L)
                .username("doctor1")
                .fullName("Dr. John Doe")
                .roles(Set.of(Role.ADMIN, Role.DOCTOR))
                .build();

        // When
        UserResponseDTO dto = mapper.toDTO(user);

        // Then
        assertEquals(1L, dto.getId());
        assertEquals("doctor1", dto.getUsername());
        assertEquals("Dr. John Doe", dto.getFullName());
        assertEquals(Set.of("ADMIN", "DOCTOR"), dto.getRoles());
    }

    @Test
    void stringToRole_shouldHandleNull() {
        assertNull(mapper.stringToRole(null));
    }

    @Test
    void roleToString_shouldHandleNull() {
        assertNull(mapper.roleToString(null));
    }
}