package com.example.exam.mappers;

import com.example.exam.entities.Prescription;
import com.example.exam.dto.prescriptiondto.PrescriptionRequestDTO;
import com.example.exam.dto.prescriptiondto.PrescriptionResponseDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PrescriptionMapper {

    @Mapping(target = "doctorName", expression = "java(entity.getDoctor().getFirstName() + \" \" + entity.getDoctor().getLastName())")
    @Mapping(target = "patientName", expression = "java(entity.getPatient().getFirstName() + \" \" + entity.getPatient().getLastName())")
    @Mapping(target = "appointmentId", source = "appointment.id")
    PrescriptionResponseDTO toDTO(Prescription entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "appointment", ignore = true)
    @Mapping(target = "doctor", ignore = true)
    @Mapping(target = "patient", ignore = true)
    Prescription toEntity(PrescriptionRequestDTO dto);
}