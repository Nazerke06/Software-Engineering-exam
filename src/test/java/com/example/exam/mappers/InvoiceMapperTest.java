package com.example.exam.mappers;

import com.example.exam.dto.invoicedto.InvoiceRequestDTO;
import com.example.exam.dto.invoicedto.InvoiceResponseDTO;
import com.example.exam.entities.Invoice;
import com.example.exam.entities.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class InvoiceMapperTest {

    private final InvoiceMapper mapper = Mappers.getMapper(InvoiceMapper.class);

    @Test
    void toResponseDTO_ShouldMapCorrectly() {
        User patient = User.builder()
                .firstName("John")
                .lastName("Doe")
                .build();

        Invoice invoice = Invoice.builder()
                .id(1L)
                .amount(100.0)
                .description("Consultation")
                .status("PAID")
                .patient(patient)
                .appointment(null)
                .build();

        InvoiceResponseDTO responseDTO = mapper.toResponseDTO(invoice);

        assertNotNull(responseDTO);
        assertEquals("John Doe", responseDTO.getPatientFullName());
        assertEquals("PENDING", responseDTO.getStatus()); // Because mapper sets constant PENDING
        assertEquals(100.0, responseDTO.getAmount());
        assertEquals("Consultation", responseDTO.getDescription());
    }

    @Test
    void toEntity_ShouldMapCorrectly() {
        InvoiceRequestDTO requestDTO = InvoiceRequestDTO.builder()
                .amount(150.0)
                .description("Follow-up")
                .appointmentId(1L)
                .build();

        Invoice invoice = mapper.toEntity(requestDTO);

        assertNotNull(invoice);
        assertEquals(150.0, invoice.getAmount());
        assertEquals("Follow-up", invoice.getDescription());
        assertEquals("PENDING", invoice.getStatus());
        assertNull(invoice.getId());
        assertNull(invoice.getAppointment());
        assertNull(invoice.getPatient());
    }
}
