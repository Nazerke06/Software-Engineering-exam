package com.example.exam.mappers;

import com.example.exam.entities.Appointment;
import com.example.exam.dto.appointmentdto.AppointmentRequestDTO;
import com.example.exam.dto.appointmentdto.AppointmentResponseDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {

    // Из сущности в Response DTO
    @Mapping(source = "doctor.fullName", target = "doctorFullName")
    @Mapping(source = "patient.fullName", target = "patientFullName")
    AppointmentResponseDTO toResponseDTO(Appointment appointment);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "doctor", ignore = true)
    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "status", constant = "SCHEDULED")
    @Mapping(target = "time", ignore = true)
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "location", ignore = true)
    Appointment toEntity(AppointmentRequestDTO requestDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "doctor", ignore = true)
    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "time", ignore = true)
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "location", ignore = true)
    void updateEntityFromDTO(AppointmentRequestDTO requestDTO, @MappingTarget Appointment appointment);
}