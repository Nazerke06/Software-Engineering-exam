package com.example.exam.mappers;

import com.example.exam.entities.Patient;
import com.example.exam.dto.patientdto.PatientRequestDTO;
import com.example.exam.dto.patientdto.PatientResponseDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PatientMapper {

    @Mapping(target = "fullName", expression = "java(entity.getFirstName() + \" \" + entity.getLastName())")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.firstName", target = "username")
    PatientResponseDTO toResponseDTO(Patient entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    Patient toEntity(PatientRequestDTO dto);
}