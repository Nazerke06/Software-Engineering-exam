package com.example.exam.dto.appointmentdto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class AppointmentResponseDTO {
    private Long id;
     LocalDateTime appointmentDate;
    private String doctorFullName;
    private String patientFullName;
    private String status;
    private String reason;
}