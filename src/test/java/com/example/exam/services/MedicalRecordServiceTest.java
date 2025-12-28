package com.example.exam.services;

import com.example.exam.dto.medicalrecorddto.MedicalRecordRequestDTO;
import com.example.exam.dto.medicalrecorddto.MedicalRecordResponseDTO;
import com.example.exam.entities.Appointment;
import com.example.exam.entities.MedicalRecord;
import com.example.exam.mappers.MedicalRecordMapper;
import com.example.exam.repositories.AppointmentRepository;
import com.example.exam.repositories.MedicalRecordRepository;
import com.example.exam.services.impl.MedicalRecordServiceImpl; // adjust if your impl has different name
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicalRecordServiceTest {

    @Mock
    private MedicalRecordRepository medicalRecordRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private MedicalRecordMapper medicalRecordMapper;

    @InjectMocks
    private MedicalRecordServiceImpl medicalRecordService; // change to your actual implementation class name

    @Test
    void createRecord_success_whenAppointmentExists() {
        // Given
        Long appointmentId = 10L;
        MedicalRecordRequestDTO requestDTO = MedicalRecordRequestDTO.builder()
                .appointmentId(appointmentId)
                .diagnosis("Common cold")
                .treatment("Rest, fluids, paracetamol")
                .build();

        Appointment existingAppointment = new Appointment();
        existingAppointment.setId(appointmentId);

        MedicalRecord mappedEntity = new MedicalRecord(); // after mapper.toEntity() - id and appointment ignored

        MedicalRecord savedEntity = new MedicalRecord();
        savedEntity.setId(50L);
        savedEntity.setMedicalRecordName("Cold - Dec 2025");
        savedEntity.setAppointment(existingAppointment);

        MedicalRecordResponseDTO responseDTO = MedicalRecordResponseDTO.builder()
                .id(50L)
                .medicalRecordName("Cold - Dec 2025")
                .appointmentId(appointmentId)
                .build();

        // Mock behavior
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(existingAppointment));
        when(medicalRecordMapper.toEntity(requestDTO)).thenReturn(mappedEntity);
        when(medicalRecordRepository.save(any(MedicalRecord.class))).thenAnswer(invocation -> {
            MedicalRecord entity = invocation.getArgument(0);
            entity.setId(50L);                    // simulate generated ID
            entity.setMedicalRecordName("Cold - Dec 2025"); // simulate name generation if done in service
            entity.setAppointment(existingAppointment);
            return entity;
        });
        when(medicalRecordMapper.toDTO(savedEntity)).thenReturn(responseDTO);

        // When
        MedicalRecordResponseDTO result = medicalRecordService.createRecord(requestDTO);

        // Then
        assertThat(result)
                .isNotNull()
                .isEqualTo(responseDTO);

        // Verify interactions
        verify(appointmentRepository).findById(appointmentId);
        verify(medicalRecordMapper).toEntity(requestDTO);
        verify(medicalRecordRepository).save(mappedEntity);
        verify(medicalRecordMapper).toDTO(savedEntity);

        // Verify that appointment was manually set before saving (common pattern)
        assertThat(mappedEntity.getAppointment()).isEqualTo(existingAppointment);
    }

    @Test
    void createRecord_throwsException_whenAppointmentNotFound() {
        // Given
        Long invalidAppointmentId = 999L;
        MedicalRecordRequestDTO requestDTO = MedicalRecordRequestDTO.builder()
                .appointmentId(invalidAppointmentId)
                .diagnosis("Test")
                .treatment("Test")
                .build();

        when(appointmentRepository.findById(invalidAppointmentId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> medicalRecordService.createRecord(requestDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Appointment not found"); // Match exact message from impl

        verify(appointmentRepository).findById(invalidAppointmentId);
        verifyNoInteractions(medicalRecordMapper, medicalRecordRepository);
    }
    }