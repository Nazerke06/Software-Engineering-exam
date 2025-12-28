package com.example.exam.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "medical_record")
@Data
@Getter
@Setter
public class MedicalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String medicalRecordName;

    @ManyToOne
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;
}
