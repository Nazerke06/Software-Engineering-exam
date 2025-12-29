package com.example.exam.dto.prescriptiondto;

import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionRequestDTO {
    private LocalDate prescriptionDate;
    private String description;
    private String dosage;
    private String medicines;
    private Long appointmentId;
    private String doctorName;

}