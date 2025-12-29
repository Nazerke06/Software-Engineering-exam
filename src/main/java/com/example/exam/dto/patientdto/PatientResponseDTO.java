package com.example.exam.dto.patientdto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientResponseDTO {
    private Long appointmentId;
    private String firstName;
    private String lastName;
    private String birthDate;
    private String gender;
    private String phone;
    private String address;
    private String insuranceNumber;
    private String fullName;        // конкатенация first + last
    private Long userId;
    private String username;
}