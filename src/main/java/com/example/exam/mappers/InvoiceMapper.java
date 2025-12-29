package com.example.exam.mappers;

import com.example.exam.entities.Invoice;
import com.example.exam.dto.invoicedto.InvoiceRequestDTO;
import com.example.exam.dto.invoicedto.InvoiceResponseDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface InvoiceMapper {

    @Mapping(
            target = "patientFullName",
            expression = "java(entity.getPatient().getFirstName() + \" \" + entity.getPatient().getLastName())"
    )
    @Mapping(source = "appointment.id", target = "appointmentId")
    @Mapping(target = "status", constant = "PENDING")
    InvoiceResponseDTO toResponseDTO(Invoice entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "appointment", ignore = true)
    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "status", constant = "PENDING")
    Invoice toEntity(InvoiceRequestDTO dto);
}