package com.example.exam.mappers;

import com.example.exam.dto.medicalrecorddto.MedicalRecordRequestDTO;
import com.example.exam.dto.medicalrecorddto.MedicalRecordResponseDTO;
import com.example.exam.entities.Appointment;
import com.example.exam.entities.MedicalRecord;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class MedicalRecordMapperTest {

    private final MedicalRecordMapper mapper = Mappers.getMapper(MedicalRecordMapper.class);

    @Test
    void toDTO_shouldMapEntityToDTO() {
        Appointment appointment = new Appointment();
        appointment.setId(10L);

        MedicalRecord record = new MedicalRecord();
        record.setId(1L);
        record.setMedicalRecordName("Test Record");
        record.setAppointment(appointment);

        MedicalRecordResponseDTO dto = mapper.toDTO(record);

        assertEquals(1L, dto.getId());
        assertEquals("Test Record", dto.getMedicalRecordName());
        assertEquals(10L, dto.getAppointmentId());
    }

    @Test
    void toEntity_shouldMapDTOToEntity() {
        MedicalRecordRequestDTO dto = MedicalRecordRequestDTO.builder()
                .appointmentId(10L)
                .diagnosis("Test Diagnosis")
                .treatment("Test Treatment")
                .build();

        MedicalRecord entity = mapper.toEntity(dto);

        // id и appointment должны быть null
        assertNull(entity.getId());
        assertNull(entity.getAppointment());
    }
}
