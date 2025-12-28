package com.example.exam.dto.medicalrecorddto;

import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MedicalRecordRequestDTO {
    private Long appointmentId;
    private String diagnosis;
    private String treatment;
}