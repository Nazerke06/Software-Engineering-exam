package com.example.exam.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
//@Table(name = "doctor_profiles")
@Data
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
public class DoctorProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fullName;

    private String specialization;
    private Integer experience;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

}
