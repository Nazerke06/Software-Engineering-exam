package com.example.exam.entities;

import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Doctors {
    Long id;
    String doctorLastName;
    String doctorName;
    String doctorEmail;
}
