package com.example.exam.services;

import com.example.exam.dto.medicalrecorddto.MedicalRecordRequestDTO;
import com.example.exam.dto.medicalrecorddto.MedicalRecordResponseDTO;
import com.example.exam.entities.Appointment;
import com.example.exam.entities.MedicalRecord;
import com.example.exam.mappers.MedicalRecordMapper;
import com.example.exam.repositories.AppointmentRepository;
import com.example.exam.repositories.MedicalRecordRepository;
import com.example.exam.services.impl.MedicalRecordServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MedicalRecordServiceTest {

    @Mock
    private MedicalRecordRepository repository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private MedicalRecordMapper mapper;

    @InjectMocks
    private MedicalRecordServiceImpl service;

    private MedicalRecordRequestDTO requestDTO;
    private Appointment appointment;
    private MedicalRecord record;
    private MedicalRecordResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        requestDTO = MedicalRecordRequestDTO.builder()
                .appointmentId(1L)
                .diagnosis("Flu")
                .treatment("Rest")
                .build();

        appointment = new Appointment();
        appointment.setId(1L);

        record = new MedicalRecord();
        record.setMedicalRecordName("Flu Record");

        responseDTO = MedicalRecordResponseDTO.builder()
                .id(1L)
                .medicalRecordName("Flu Record")
                .appointmentId(1L)
                .build();
    }

    @Test
    void createRecord_success() {
        // Мокаем вызовы
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(mapper.toEntity(requestDTO)).thenReturn(record);
        when(repository.save(record)).thenReturn(record);
        when(mapper.toDTO(record)).thenReturn(responseDTO);

        MedicalRecordResponseDTO result = service.createRecord(requestDTO);

        // Проверка результата
        assertNotNull(result);
        assertEquals("Flu Record", result.getMedicalRecordName());
        assertEquals(1L, result.getAppointmentId());

        // Проверка, что appointment был установлен в entity
        assertEquals(appointment, record.getAppointment());

        verify(repository, times(1)).save(record);
        verify(mapper, times(1)).toEntity(requestDTO);
        verify(mapper, times(1)).toDTO(record);
    }

    @Test
    void createRecord_appointmentNotFound() {
        when(appointmentRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> service.createRecord(requestDTO));

        assertEquals("Appointment not found", exception.getMessage());
    }
}
