package com.example.exam.dto.invoicedto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceRequestDTO {
    private Double amount;
    private String description;
    private Long appointmentId;
}