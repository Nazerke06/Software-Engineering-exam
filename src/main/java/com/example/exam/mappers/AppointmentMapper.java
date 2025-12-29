package com.example.exam.mappers;

import com.example.exam.entities.Appointment;
import com.example.exam.dto.appointmentdto.AppointmentRequestDTO;
import com.example.exam.dto.appointmentdto.AppointmentResponseDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {

    @Mapping(target = "doctorName", expression = "java(entity.getDoctor().getUser().getFirstName() + \" \" + entity.getDoctor().getUser().getLastName())")
    @Mapping(target = "patientName", expression = "java(entity.getPatient().getUser().getFirstName() + \" \" + entity.getPatient().getUser().getLastName())")
    @Mapping(target = "status", expression = "java(entity.getStatus().name())")
    @Mapping(target = "reason", source = "description")
    AppointmentResponseDTO toDTO(Appointment entity);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "doctor", ignore = true)
    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "status", constant = "BOOKED")
    @Mapping(target = "appointmentDate", ignore = true)
    @Mapping(target = "description", ignore = true)
    Appointment toEntity(AppointmentRequestDTO requestDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "doctor", ignore = true)
    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "appointmentDate", ignore = true)
    @Mapping(target = "description", ignore = true)
    void updateEntityFromDTO(AppointmentRequestDTO requestDTO, @MappingTarget Appointment appointment);
}