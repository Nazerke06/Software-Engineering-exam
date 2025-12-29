package com.example.exam.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "doctors")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DoctorProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    private String specialization; // e.g., Cardiologist
    private Integer experienceYears;
    private String qualification;
}