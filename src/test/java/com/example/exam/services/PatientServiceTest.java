package com.example.exam.services;

import com.example.exam.dto.patientdto.PatientRequestDTO;
import com.example.exam.dto.patientdto.PatientResponseDTO;
import com.example.exam.entities.Patient;
import com.example.exam.entities.Role;
import com.example.exam.entities.User;
import com.example.exam.mappers.PatientMapper;
import com.example.exam.repositories.PatientRepository;
import com.example.exam.services.impl.PatientServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private PatientMapper patientMapper;

    @InjectMocks
    private PatientServiceImpl patientService;

    private User user;
    private Patient patient;
    private PatientRequestDTO requestDTO;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        user = User.builder()
                .id(1L)
                .firstName("user123")
                .role(Role.valueOf("PATIENT"))
                .build();

        patient = Patient.builder()
                .id(10L)
                .firstName("John")
                .lastName("Doe")
                .user(user)
                .build();

        requestDTO = PatientRequestDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .build();
    }

    @Test
    void createPatientProfile_success() {
        when(patientRepository.findByUser(user)).thenReturn(Optional.empty());
        when(patientMapper.toEntity(requestDTO)).thenReturn(patient);
        when(patientRepository.save(patient)).thenReturn(patient);
        when(patientMapper.toResponseDTO(patient)).thenReturn(
                PatientResponseDTO.builder()
                        .fullName("John Doe")
                        .userId(1L)
                        .build()
        );

        PatientResponseDTO response = patientService.createPatientProfile(requestDTO, user);

        assertEquals("John Doe", response.getFullName());
        assertEquals(1L, response.getUserId());
        verify(patientRepository, times(1)).save(patient);
    }

    @Test
    void createPatientProfile_alreadyExists_throwsException() {
        when(patientRepository.findByUser(user)).thenReturn(Optional.of(patient));

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                patientService.createPatientProfile(requestDTO, user));

        assertEquals("Patient profile already exists for this user", ex.getMessage());
    }

    @Test
    void updatePatientProfile_success() {
        when(patientRepository.findById(10L)).thenReturn(Optional.of(patient));
        when(patientRepository.save(patient)).thenReturn(patient);
        when(patientMapper.toResponseDTO(patient)).thenReturn(
                PatientResponseDTO.builder()
                        .fullName("John Doe")
                        .userId(1L)
                        .build()
        );

        PatientResponseDTO response = patientService.updatePatientProfile(10L, requestDTO, user);

        assertEquals("John Doe", response.getFullName());
        verify(patientRepository, times(1)).save(patient);
    }

    @Test
    void updatePatientProfile_notOwner_throwsException() {
        User otherUser = User.builder().id(2L).role(Role.valueOf("PATIENT")).build();
        when(patientRepository.findById(10L)).thenReturn(Optional.of(patient));

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                patientService.updatePatientProfile(10L, requestDTO, otherUser));

        assertEquals("You are not allowed to update this profile", ex.getMessage());
    }

    @Test
    void getPatientById_notOwnerOrAdmin_throwsException() {
        User otherUser = User.builder().id(2L).role(Role.valueOf("PATIENT")).build();
        when(patientRepository.findById(10L)).thenReturn(Optional.of(patient));

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                patientService.getPatientById(10L, otherUser));

        assertEquals("You are not allowed to view this profile", ex.getMessage());
    }
}
