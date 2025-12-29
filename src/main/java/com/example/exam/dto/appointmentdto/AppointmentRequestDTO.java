package com.example.exam.dto.appointmentdto;

import lombok.*;

import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AppointmentRequestDTO {
     LocalDateTime appointmentDate;
    private Long doctorId;
    private String doctorName;
    private String reason;
}