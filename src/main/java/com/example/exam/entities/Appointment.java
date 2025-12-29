package com.example.exam.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private DoctorProfile doctor;
//    private LocalDateTime appointmentTime;

    private LocalDateTime appointmentDate;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status = AppointmentStatus.BOOKED;

    private String description;

    public enum AppointmentStatus {
        BOOKED, COMPLETED, CANCELLED
    }
}