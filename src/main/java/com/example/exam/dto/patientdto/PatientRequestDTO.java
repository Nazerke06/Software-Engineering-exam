package com.example.exam.dto.patientdto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientRequestDTO {
    private String firstName;
    private String lastName;
    private String birthDate;
    private String gender;
    private String phone;
    private String address;
    private String insuranceNumber;
    private Long userId;
}