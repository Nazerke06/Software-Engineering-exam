package com.example.exam.mappers;

import com.example.exam.entities.MedicalRecord;
import com.example.exam.dto.medicalrecorddto.MedicalRecordRequestDTO;
import com.example.exam.dto.medicalrecorddto.MedicalRecordResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MedicalRecordMapper {

    @Mapping(source = "appointment.id", target = "appointmentId")
    MedicalRecordResponseDTO toDTO(MedicalRecord entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "appointment", ignore = true) // назначаем в сервисе
    MedicalRecord toEntity(MedicalRecordRequestDTO dto);
}

