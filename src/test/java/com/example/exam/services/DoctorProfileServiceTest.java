package com.example.exam.services;

import com.example.exam.dto.doctorprofiledto.DoctorProfileRequestDTO;
import com.example.exam.dto.doctorprofiledto.DoctorProfileResponseDTO;
import com.example.exam.entities.DoctorProfile;
import com.example.exam.entities.User;
import com.example.exam.mappers.DoctorProfileMapper;
import com.example.exam.repositories.DoctorProfileRepository;
import com.example.exam.repositories.UserRepository;
import com.example.exam.services.impl.DoctorProfileServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DoctorProfileServiceTest {

    @InjectMocks
    private DoctorProfileServiceImpl service;

    @Mock
    private DoctorProfileRepository doctorProfileRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private DoctorProfileMapper doctorProfileMapper;

    private User user;
    private DoctorProfileRequestDTO requestDTO;
    private DoctorProfile doctorProfile;
    private DoctorProfileResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = User.builder().id(1L).firstName("John").lastName("Doe").build();
        requestDTO = DoctorProfileRequestDTO.builder()
                .userId(1L)
                .specialization("Cardiologist")
                .experience(10)
                .build();
        doctorProfile = DoctorProfile.builder()
                .id(1L)
                .user(user)
                .specialization("Cardiologist")
                .experienceYears(10)
                .build();
        responseDTO = DoctorProfileResponseDTO.builder()
                .id(1L)
                .doctorName("John Doe")
                .specialization("Cardiologist")
                .experience(10)
                .build();
    }

    @Test
    void createProfile_success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(doctorProfileRepository.findByUser(user)).thenReturn(Optional.empty());
        when(doctorProfileMapper.toEntity(requestDTO)).thenReturn(doctorProfile);
        when(doctorProfileRepository.save(doctorProfile)).thenReturn(doctorProfile);
        when(doctorProfileMapper.toDTO(doctorProfile)).thenReturn(responseDTO);

        DoctorProfileResponseDTO result = service.createProfile(requestDTO, user);

        assertNotNull(result);
        assertEquals("John Doe", result.getDoctorName());
        verify(doctorProfileRepository).save(doctorProfile);
    }

    @Test
    void createProfile_userNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> service.createProfile(requestDTO, user));

        assertEquals("User not found with id: 1", exception.getMessage());
    }

    @Test
    void createProfile_alreadyExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(doctorProfileRepository.findByUser(user)).thenReturn(Optional.of(doctorProfile));

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> service.createProfile(requestDTO, user));

        assertEquals("Doctor profile already exists for this user", exception.getMessage());
    }

    @Test
    void deleteProfile_success() {
        when(doctorProfileRepository.findById(1L)).thenReturn(Optional.of(doctorProfile));

        service.deleteProfile(1L, user);

        verify(doctorProfileRepository).delete(doctorProfile);
    }

    @Test
    void deleteProfile_notFound() {
        when(doctorProfileRepository.findById(1L)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> service.deleteProfile(1L, user));

        assertEquals("Doctor profile not found with id: 1", exception.getMessage());
    }
}
