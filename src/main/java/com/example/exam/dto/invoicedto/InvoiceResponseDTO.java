package com.example.exam.dto.invoicedto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceResponseDTO {
    private Long id;
    private Double amount;
    private String description;
    private String status;
    private String patientFullName;
    private Long appointmentId;
}