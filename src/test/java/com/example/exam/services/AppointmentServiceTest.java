package com.example.exam.services;

import com.example.exam.dto.appointmentdto.AppointmentRequestDTO;
import com.example.exam.dto.appointmentdto.AppointmentResponseDTO;
import com.example.exam.entities.Appointment;
import com.example.exam.entities.Role;
import com.example.exam.entities.User;
import com.example.exam.mappers.AppointmentMapper;
import com.example.exam.repositories.AppointmentRepository;
import com.example.exam.repositories.UserRepository;
import com.example.exam.services.impl.AppointmentServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AppointmentMapper appointmentMapper;

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    private User createUser(Long id, String fullName, Role... roles) {
        User user = new User();
        user.setId(id);
        user.setFullName(fullName);
        user.setRoles(new HashSet<>(Arrays.asList(roles)));
        return user;
    }

    private User createPatient() {
        return createUser(1L, "John Doe", Role.PATIENT);
    }

    private User createDoctor() {
        return createUser(10L, "Dr. Sarah Connor", Role.DOCTOR);
    }

    private User createAdmin() {
        return createUser(99L, "Admin User", Role.ADMIN);
    }

    private AppointmentRequestDTO createRequestDTO() {
        return AppointmentRequestDTO.builder()
                .appointmentDate(LocalDateTime.of(2025, 12, 30, 14, 30))
                .doctorId(10L)
                .reason("Regular check-up")
                .build();
    }

    @Test
    @DisplayName("createAppointment - should successfully create appointment for patient")
    void createAppointment_success() {
        AppointmentRequestDTO requestDTO = createRequestDTO();
        User patient = createPatient();
        User doctor = createDoctor();

        Appointment mappedEntity = new Appointment();
        mappedEntity.setAppointmentDate(requestDTO.getAppointmentDate());
        mappedEntity.setReason(requestDTO.getReason());
        mappedEntity.setStatus("SCHEDULED");

        Appointment savedEntity = new Appointment();
        savedEntity.setId(100L);
        savedEntity.setAppointmentDate(requestDTO.getAppointmentDate());
        savedEntity.setReason(requestDTO.getReason());
        savedEntity.setStatus("SCHEDULED");
        savedEntity.setDoctor(doctor);
        savedEntity.setPatient(patient);

        AppointmentResponseDTO responseDTO = AppointmentResponseDTO.builder()
                .id(100L)
                .appointmentDate(requestDTO.getAppointmentDate())
                .doctorFullName("Dr. Sarah Connor")
                .patientFullName("John Doe")
                .status("SCHEDULED")
                .reason("Regular check-up")
                .build();

        when(appointmentMapper.toEntity(requestDTO)).thenReturn(mappedEntity);
        when(userRepository.findById(10L)).thenReturn(Optional.of(doctor));
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(savedEntity);
        when(appointmentMapper.toResponseDTO(savedEntity)).thenReturn(responseDTO);

        AppointmentResponseDTO result = appointmentService.createAppointment(requestDTO, patient);

        assertNotNull(result);
        assertEquals(100L, result.getId());
        assertEquals("Dr. Sarah Connor", result.getDoctorFullName());
        assertEquals("John Doe", result.getPatientFullName());
        assertEquals("SCHEDULED", result.getStatus());

        verify(appointmentRepository).save(any());
    }

    @Test
    @DisplayName("createAppointment - should throw if doctor not found")
    void createAppointment_doctorNotFound() {
        AppointmentRequestDTO requestDTO = createRequestDTO();
        User patient = createPatient();

        // Only stub what's actually used before the exception
        when(userRepository.findById(10L)).thenReturn(Optional.empty());

        // No other stubbings here — mapper and save should NOT be called

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                appointmentService.createAppointment(requestDTO, patient));

        String message = exception.getMessage().toLowerCase();
        assertTrue(message.contains("not found") ||
                message.contains("doctor") ||
                message.contains("user"));

        verify(appointmentRepository, never()).save(any());
        verify(appointmentMapper, never()).toEntity(any());
    }

    @Test
    @DisplayName("cancelAppointment - should cancel appointment if called by admin")
    void cancelAppointment_success_byAdmin() {
        Long appointmentId = 100L;
        User admin = createAdmin();

        Appointment existing = new Appointment();
        existing.setId(appointmentId);
        existing.setStatus("SCHEDULED");
        existing.setDoctor(createDoctor());
        existing.setPatient(createPatient());

        Appointment cancelled = new Appointment();
        cancelled.setId(appointmentId);
        cancelled.setStatus("CANCELED");  // ← Your actual spelling: one L
        cancelled.setDoctor(existing.getDoctor());
        cancelled.setPatient(existing.getPatient());

        AppointmentResponseDTO responseDTO = AppointmentResponseDTO.builder()
                .id(appointmentId)
                .status("CANCELED")
                .doctorFullName("Dr. Sarah Connor")
                .patientFullName("John Doe")
                .build();

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(existing));
        when(appointmentRepository.save(existing)).thenReturn(cancelled);
        when(appointmentMapper.toResponseDTO(cancelled)).thenReturn(responseDTO);

        AppointmentResponseDTO result = appointmentService.cancelAppointment(appointmentId, admin);

        assertEquals("CANCELED", result.getStatus());
        assertEquals("CANCELED", existing.getStatus());  // mutated in place
        verify(appointmentRepository).save(existing);
    }

    @Test
    @DisplayName("cancelAppointment - should throw if appointment not found")
    void cancelAppointment_notFound() {
        Long id = 999L;
        when(appointmentRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                appointmentService.cancelAppointment(id, createAdmin()));

        String message = exception.getMessage().toLowerCase();
        assertTrue(message.contains("appointment") || message.contains("not found"));

        verify(appointmentRepository, never()).save(any());
    }

    @Test
    @DisplayName("getAllAppointments - should return list of all appointments")
    void getAllAppointments_returnsList() {
        Appointment appointment = new Appointment();
        appointment.setId(1L);
        appointment.setStatus("SCHEDULED");
        appointment.setDoctor(createDoctor());
        appointment.setPatient(createPatient());

        AppointmentResponseDTO dto = AppointmentResponseDTO.builder()
                .id(1L)
                .status("SCHEDULED")
                .doctorFullName("Dr. Sarah Connor")
                .patientFullName("John Doe")
                .build();

        when(appointmentRepository.findAll()).thenReturn(List.of(appointment));
        when(appointmentMapper.toResponseDTO(appointment)).thenReturn(dto);

        List<AppointmentResponseDTO> result = appointmentService.getAllAppointments();

        assertEquals(1, result.size());
        assertEquals("Dr. Sarah Connor", result.get(0).getDoctorFullName());
    }

    @Test
    @DisplayName("getAllAppointments - should return empty list when none exist")
    void getAllAppointments_empty() {
        when(appointmentRepository.findAll()).thenReturn(Collections.emptyList());

        List<AppointmentResponseDTO> result = appointmentService.getAllAppointments();

        assertTrue(result.isEmpty());
    }
}