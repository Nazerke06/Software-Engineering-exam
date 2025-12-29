package com.example.exam.mappers;

import com.example.exam.dto.appointmentdto.AppointmentRequestDTO;
import com.example.exam.dto.appointmentdto.AppointmentResponseDTO;
import com.example.exam.entities.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class AppointmentMapperTest {

    private AppointmentMapper mapper;

    @BeforeEach
    void setup() {
        mapper = Mappers.getMapper(AppointmentMapper.class);
    }

    @Test
    void testToDTO() {
        // Setup doctor and patient users
        User doctorUser = new User();
        doctorUser.setFirstName("Sarah");
        doctorUser.setLastName("Connor");
        doctorUser.setRole(Role.DOCTOR);

        User patientUser = new User();
        patientUser.setFirstName("John");
        patientUser.setLastName("Doe");
        patientUser.setRole(Role.PATIENT);

        // Setup doctor profile and patient profile
        DoctorProfile doctorProfile = new DoctorProfile();
        doctorProfile.setUser(doctorUser);

        Patient patient = new Patient();
        patient.setUser(patientUser);

        // Setup appointment entity
        Appointment appointment = new Appointment();
        appointment.setId(1L);
        appointment.setDoctor(doctorProfile);
        appointment.setPatient(patient);
        appointment.setAppointmentDate(LocalDateTime.of(2025, 12, 30, 14, 0));
        appointment.setStatus(Appointment.AppointmentStatus.BOOKED);
        appointment.setDescription("Regular checkup");

        // Map to DTO
        AppointmentResponseDTO dto = mapper.toDTO(appointment);

        assertNotNull(dto);
        assertEquals("Sarah Connor", dto.getDoctorName());
        assertEquals("John Doe", dto.getPatientName());
        assertEquals("BOOKED", dto.getStatus());
        assertEquals("Regular checkup", dto.getReason());
    }

    @Test
    void testToEntity() {
        AppointmentRequestDTO requestDTO = AppointmentRequestDTO.builder()
                .doctorId(10L)
                .appointmentDate(LocalDateTime.of(2025, 12, 30, 14, 0))
                .reason("Regular checkup")
                .build();

        Appointment entity = mapper.toEntity(requestDTO);

        assertNotNull(entity);
        assertEquals(Appointment.AppointmentStatus.BOOKED, entity.getStatus());
        assertNull(entity.getPatient());
        assertNull(entity.getDoctor());
        assertNull(entity.getDescription()); // because mapping ignores description
    }

    @Test
    void testUpdateEntityFromDTO() {
        AppointmentRequestDTO requestDTO = AppointmentRequestDTO.builder()
                .doctorId(20L)
                .appointmentDate(LocalDateTime.of(2025, 12, 31, 10, 0))
                .reason("Updated reason")
                .build();

        Appointment entity = new Appointment();
        entity.setStatus(Appointment.AppointmentStatus.BOOKED);

        mapper.updateEntityFromDTO(requestDTO, entity);

        assertNotNull(entity);
        assertEquals(Appointment.AppointmentStatus.BOOKED, entity.getStatus()); // unchanged
        assertNull(entity.getDoctor());
        assertNull(entity.getPatient());
        assertNull(entity.getDescription()); // ignored in update mapping
    }
}