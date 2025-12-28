package com.example.exam.services.impl;

import com.example.exam.dto.medicalrecorddto.MedicalRecordRequestDTO;
import com.example.exam.dto.medicalrecorddto.MedicalRecordResponseDTO;
import com.example.exam.entities.Appointment;
import com.example.exam.entities.MedicalRecord;
import com.example.exam.mappers.MedicalRecordMapper;
import com.example.exam.repositories.AppointmentRepository;
import com.example.exam.repositories.MedicalRecordRepository;
import com.example.exam.services.MedicalRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private final MedicalRecordRepository repository;
    private final MedicalRecordMapper mapper;
    private final AppointmentRepository appointmentRepository;

    @Override
    public MedicalRecordResponseDTO createRecord(MedicalRecordRequestDTO dto) {
        Appointment appointment = appointmentRepository.findById(dto.getAppointmentId())
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        MedicalRecord record = mapper.toEntity(dto);
        record.setAppointment(appointment);

        return mapper.toDTO(repository.save(record));
    }
}
