package com.example.exam.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "appointments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime appointmentDate;
    private String time;
    private String description;
    private String location;
    private String status;
    private String reason;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private User patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private User doctor;
}
