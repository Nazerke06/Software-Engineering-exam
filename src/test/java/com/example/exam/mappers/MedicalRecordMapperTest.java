package com.example.exam.mappers;

import com.example.exam.entities.Appointment;
import com.example.exam.entities.MedicalRecord;
import com.example.exam.dto.medicalrecorddto.MedicalRecordRequestDTO;
import com.example.exam.dto.medicalrecorddto.MedicalRecordResponseDTO;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

class MedicalRecordMapperTest {

    private final MedicalRecordMapper mapper = Mappers.getMapper(MedicalRecordMapper.class);

    @Test
    void toDTO_shouldMapCorrectly() {
        // Given
        Appointment appointment = new Appointment();
        appointment.setId(1L);

        MedicalRecord entity = new MedicalRecord();
        entity.setId(100L);
        entity.setMedicalRecordName("Hypertension Record");
        entity.setAppointment(appointment);

        // When
        MedicalRecordResponseDTO dto = mapper.toDTO(entity);

        // Then
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(100L);
        assertThat(dto.getMedicalRecordName()).isEqualTo("Hypertension Record");
        assertThat(dto.getAppointmentId()).isEqualTo(1L);
    }

    @Test
    void toEntity_shouldMapRequestDTOAndIgnoreIdAndAppointment() {
        // Given
        MedicalRecordRequestDTO dto = MedicalRecordRequestDTO.builder()
                .appointmentId(5L)
                .diagnosis("Diabetes")
                .treatment("Insulin therapy")
                .build();

        // When
        MedicalRecord entity = mapper.toEntity(dto);

        // Then
        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isNull();
        assertThat(entity.getMedicalRecordName()).isNull();
    }
}