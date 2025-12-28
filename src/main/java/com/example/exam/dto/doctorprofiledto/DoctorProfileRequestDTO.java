package com.example.exam.dto.doctorprofiledto;

import lombok.*;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DoctorProfileRequestDTO {
    private String specialization;
    private Integer experience;
    private Long userId;
}
