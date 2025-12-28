package com.example.exam.dto.doctorprofiledto;

import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DoctorProfileResponseDTO {
    private Long id;
    private String doctorName;
    private String specialization;
    private Integer experience;
}