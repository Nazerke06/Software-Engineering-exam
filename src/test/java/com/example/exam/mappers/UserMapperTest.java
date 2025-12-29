package com.example.exam.mappers;

import com.example.exam.dto.userdto.UserRequestDTO;
import com.example.exam.dto.userdto.UserResponseDTO;
import com.example.exam.entities.Role;
import com.example.exam.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        // Если используем MapStruct без Spring, создаем через имплементацию
        userMapper = new UserMapperImpl();
    }

    @Test
    void toEntity_mapsCorrectly() {
        UserRequestDTO dto = UserRequestDTO.builder()
                .username("doctor1")
                .password("secret")
                .fullName("Doctor One")
                .roles(Set.of("DOCTOR"))
                .build();

        User entity = userMapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals("doctor1", entity.getUsername());
        assertEquals(Role.DOCTOR, entity.getRole());
        assertNull(entity.getPassword()); // password игнорируется в маппере
    }

    @Test
    void toDTO_mapsCorrectly() {
        User user = User.builder()
                .id(1L)
                .username("doctor1")
                .firstName("Doctor")
                .lastName("One")
                .role(Role.DOCTOR)
                .build();

        UserResponseDTO dto = userMapper.toDTO(user);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("doctor1", dto.getUsername());
        assertEquals(Set.of("DOCTOR"), dto.getRoles());
    }

    @Test
    void stringToRole_handlesEmptySet() {
        assertNull(userMapper.stringToRole(Set.of()));
        assertNull(userMapper.stringToRole(null));
    }

    @Test
    void roleToSet_handlesNull() {
        assertNull(userMapper.roleToSet(null));
        assertEquals(Set.of("ADMIN"), userMapper.roleToSet(Role.ADMIN));
    }
}
