package com.example.exam.services;

import com.example.exam.dto.userdto.UserRequestDTO;
import com.example.exam.dto.userdto.UserResponseDTO;
import com.example.exam.entities.Role;
import com.example.exam.entities.User;
import com.example.exam.mappers.UserMapper;
import com.example.exam.repositories.UserRepository;
import com.example.exam.services.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService; // <- your actual implementation class

    @Test
    void createUser_shouldEncodePassword_andSaveUser() {
        // Given
        UserRequestDTO dto = UserRequestDTO.builder()
                .username("newuser")
                .password("plainPassword")
                .fullName("New User")
                .roles(Set.of("DOCTOR"))
                .build();

        User admin = User.builder().roles(Set.of(Role.ADMIN)).build();

        User mappedUser = User.builder()
                .username("newuser")
                .fullName("New User")
                .roles(Set.of(Role.DOCTOR))
                .build();

        User savedUser = User.builder()
                .id(10L)
                .username("newuser")
                .fullName("New User")
                .password("encodedPassword")
                .roles(Set.of(Role.DOCTOR))
                .enabled(true)
                .build();

        UserResponseDTO expectedDto = UserResponseDTO.builder()
                .id(10L)
                .username("newuser")
                .fullName("New User")
                .roles(Set.of("DOCTOR"))
                .build();

        when(userMapper.toEntity(dto)).thenReturn(mappedUser);
        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(userMapper.toDTO(savedUser)).thenReturn(expectedDto);

        // When
        UserResponseDTO result = userService.createUser(dto, admin);

        // Then
        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals("newuser", result.getUsername());

        verify(userMapper).toEntity(dto);
        verify(passwordEncoder).encode("plainPassword");
        verify(userRepository).save(argThat(u ->
                "encodedPassword".equals(u.getPassword()) && u.isEnabled()
        ));
        verify(userMapper).toDTO(savedUser);
    }

    @Test
    void getAllUsers_shouldReturnMappedList() {
        // Given
        User user1 = User.builder().id(1L).username("u1").build();
        User user2 = User.builder().id(2L).username("u2").build();

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));
        when(userMapper.toDTO(user1)).thenReturn(new UserResponseDTO(1L, "u1", null, null));
        when(userMapper.toDTO(user2)).thenReturn(new UserResponseDTO(2L, "u2", null, null));

        // When
        List<UserResponseDTO> result = userService.getAllUsers();

        // Then
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
        verify(userRepository).findAll();
    }

    @Test
    void getUserById_shouldReturnMappedDto() {
        // Given
        Long id = 5L;
        User user = User.builder().id(id).username("test").build();
        UserResponseDTO dto = new UserResponseDTO(id, "test", null, null);

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userMapper.toDTO(user)).thenReturn(dto);

        // When
        UserResponseDTO result = userService.getUserById(id);

        // Then
        assertEquals(dto, result);
    }

    @Test
    void deleteUser_shouldCallRepositoryDelete() {
        // Given
        Long id = 3L;
        User admin = User.builder().roles(Set.of(Role.ADMIN)).build();

        // When
        userService.deleteUser(id, admin);

        // Then
        verify(userRepository).deleteById(id);
    }
}