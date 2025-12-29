package com.example.exam.services;

import com.example.exam.dto.userdto.UserRequestDTO;
import com.example.exam.dto.userdto.UserResponseDTO;
import com.example.exam.entities.Role;
import com.example.exam.entities.User;
import com.example.exam.mappers.UserMapper;
import com.example.exam.repositories.UserRepository;
import com.example.exam.services.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User adminUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        adminUser = User.builder()
                .id(1L)
                .username("admin")
                .role(Role.ADMIN)
                .build();
    }

    @Test
    void createUser_asAdmin_success() {
        UserRequestDTO requestDTO = UserRequestDTO.builder()
                .username("doctor1")
                .password("password")
                .fullName("Doctor One")
                .roles(Set.of("DOCTOR"))
                .build();

        User mappedUser = User.builder()
                .username("doctor1")
                .role(Role.DOCTOR)
                .build();

        User savedUser = User.builder()
                .id(2L)
                .username("doctor1")
                .role(Role.DOCTOR)
                .password("encodedPassword")
                .build();

        UserResponseDTO responseDTO = UserResponseDTO.builder()
                .id(2L)
                .username("doctor1")
                .roles(Set.of("DOCTOR"))
                .build();

        when(userMapper.toEntity(requestDTO)).thenReturn(mappedUser);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(mappedUser)).thenReturn(savedUser);
        when(userMapper.toDTO(savedUser)).thenReturn(responseDTO);

        UserResponseDTO result = userService.createUser(requestDTO, adminUser);

        assertEquals("doctor1", result.getUsername());
        assertEquals(Set.of("DOCTOR"), result.getRoles());
        verify(userRepository, times(1)).save(mappedUser);
    }

    @Test
    void createUser_asNonAdmin_throwsException() {
        User doctor = User.builder().role(Role.DOCTOR).build();
        UserRequestDTO dto = new UserRequestDTO();

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.createUser(dto, doctor));

        assertEquals("Only admins can create users", exception.getMessage());
    }

    @Test
    void getAllUsers_returnsMappedList() {
        User user1 = User.builder().id(1L).username("u1").role(Role.DOCTOR).build();
        User user2 = User.builder().id(2L).username("u2").role(Role.ADMIN).build();

        UserResponseDTO dto1 = UserResponseDTO.builder().id(1L).username("u1").roles(Set.of("DOCTOR")).build();
        UserResponseDTO dto2 = UserResponseDTO.builder().id(2L).username("u2").roles(Set.of("ADMIN")).build();

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));
        when(userMapper.toDTO(user1)).thenReturn(dto1);
        when(userMapper.toDTO(user2)).thenReturn(dto2);

        List<UserResponseDTO> result = userService.getAllUsers();

        assertEquals(2, result.size());
        assertEquals("u1", result.get(0).getUsername());
        assertEquals("u2", result.get(1).getUsername());
    }

    @Test
    void getUserById_existingUser_returnsDTO() {
        User user = User.builder().id(1L).username("u1").role(Role.DOCTOR).build();
        UserResponseDTO dto = UserResponseDTO.builder().id(1L).username("u1").roles(Set.of("DOCTOR")).build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toDTO(user)).thenReturn(dto);

        UserResponseDTO result = userService.getUserById(1L);

        assertEquals("u1", result.getUsername());
    }

    @Test
    void getUserById_nonExistingUser_throwsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.getUserById(1L));

        assertEquals("User not found with id: 1", exception.getMessage());
    }

    @Test
    void deleteUser_asAdmin_success() {
        when(userRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> userService.deleteUser(1L, adminUser));
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteUser_asNonAdmin_throwsException() {
        User doctor = User.builder().role(Role.DOCTOR).build();
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.deleteUser(1L, doctor));
        assertEquals("Only admins can delete users", exception.getMessage());
    }

    @Test
    void deleteUser_nonExistingUser_throwsException() {
        when(userRepository.existsById(1L)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.deleteUser(1L, adminUser));

        assertEquals("User not found with id: 1", exception.getMessage());
    }
}
