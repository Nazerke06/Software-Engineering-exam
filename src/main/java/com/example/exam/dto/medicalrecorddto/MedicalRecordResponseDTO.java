package com.example.exam.dto.medicalrecorddto;

import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MedicalRecordResponseDTO {
    private Long id;
    private String medicalRecordName;
    private Long appointmentId;
}
