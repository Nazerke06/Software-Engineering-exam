package com.example.exam.entities;

import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Appointments {
    Long id;
    String appointmentDate;
    String appointmentTime;
    String appointmentDescription;
    String appointmentLocation;


}
