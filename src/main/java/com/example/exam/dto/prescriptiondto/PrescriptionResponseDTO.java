package com.example.exam.dto.prescriptiondto;

import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionResponseDTO {
    private Long id;
    private LocalDate prescriptionDate;
    private String description;
    private String dosage;
    private String medicines;
    private String doctorName;
    private String patientName;
    private Long appointmentId;
}