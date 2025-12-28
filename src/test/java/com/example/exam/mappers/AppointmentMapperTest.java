package com.example.exam.mappers;

import com.example.exam.dto.appointmentdto.AppointmentRequestDTO;
import com.example.exam.dto.appointmentdto.AppointmentResponseDTO;
import com.example.exam.entities.Appointment;
import com.example.exam.entities.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AppointmentMapperTest {

    private final AppointmentMapper mapper = Mappers.getMapper(AppointmentMapper.class);

    private User createDoctor(String fullName) {
        User doctor = new User();
        doctor.setId(10L);
        doctor.setFullName(fullName);
        return doctor;
    }

    private User createPatient(String fullName) {
        User patient = new User();
        patient.setId(20L);
        patient.setFullName(fullName);
        return patient;
    }

    private AppointmentRequestDTO createRequestDTO() {
        return AppointmentRequestDTO.builder()
                .appointmentDate(LocalDateTime.of(2025, 12, 30, 14, 30))
                .doctorId(10L)
                .reason("Annual check-up")
                .build();
    }

    private Appointment createFullAppointment() {
        Appointment appointment = new Appointment();
        appointment.setId(1L);
        // Use the exact same LocalDateTime as in the DTO
        appointment.setAppointmentDate(LocalDateTime.of(2025, 12, 30, 14, 30));
        appointment.setReason("Annual check-up");
        appointment.setStatus("SCHEDULED");
        appointment.setDoctor(createDoctor("Dr. Emily Johnson"));
        appointment.setPatient(createPatient("Alex Smith"));

        return appointment;
    }

    @Test
    @DisplayName("toResponseDTO should correctly map entity to response DTO including doctor and patient full names")
    void toResponseDTO_mapsCorrectly() {
        Appointment appointment = createFullAppointment();

        AppointmentResponseDTO dto = mapper.toResponseDTO(appointment);

        assertNotNull(dto);
        assertEquals(appointment.getId(), dto.getId());
        assertEquals(appointment.getAppointmentDate(), dto.getAppointmentDate());
        assertEquals(appointment.getReason(), dto.getReason());
        assertEquals(appointment.getStatus(), dto.getStatus());
        assertEquals("Dr. Emily Johnson", dto.getDoctorFullName());
        assertEquals("Alex Smith", dto.getPatientFullName());
    }

    @Test
    @DisplayName("toEntity should map request DTO to new entity with constant status and ignore specified fields")
    void toEntity_createsEntityWithDefaults() {
        AppointmentRequestDTO requestDTO = createRequestDTO();

        Appointment appointment = mapper.toEntity(requestDTO);

        assertNull(appointment.getId());
        assertNull(appointment.getDoctor());
        assertNull(appointment.getPatient());
        assertEquals("SCHEDULED", appointment.getStatus());

        assertEquals(requestDTO.getAppointmentDate(), appointment.getAppointmentDate());
        assertEquals(requestDTO.getReason(), appointment.getReason());
    }

    @Test
    @DisplayName("updateEntityFromDTO should update only allowed fields and ignore id, doctor, patient, status")
    void updateEntityFromDTO_updatesSelectiveFields() {
        Appointment existing = createFullAppointment();
        // keep original values that should not change
        existing.setReason("Old reason");
        existing.setAppointmentDate(LocalDateTime.of(2025, 1, 1, 10, 0));

        AppointmentRequestDTO updateDTO = AppointmentRequestDTO.builder()
                .appointmentDate(LocalDateTime.of(2026, 1, 15, 15, 0))
                .reason("Follow-up visit")
                .doctorId(99L) // ignored
                .build();

        mapper.updateEntityFromDTO(updateDTO, existing);

        // unchanged fields
        assertEquals(1L, existing.getId());
        assertEquals("Dr. Emily Johnson", existing.getDoctor().getFullName());
        assertEquals("Alex Smith", existing.getPatient().getFullName());
        assertEquals("SCHEDULED", existing.getStatus());

        // updated fields
        assertEquals(updateDTO.getAppointmentDate(), existing.getAppointmentDate());
        assertEquals(updateDTO.getReason(), existing.getReason());
    }

    @Test
    @DisplayName("toResponseDTO should return null when input is null")
    void toResponseDTO_handlesNull() {
        assertNull(mapper.toResponseDTO(null));
    }

    @Test
    @DisplayName("toEntity should return null when source DTO is null (default MapStruct behaviour)")
    void toEntity_handlesNull() {
        // MapStruct returns null for null source in object → object mappings
        assertNull(mapper.toEntity(null));
    }
}