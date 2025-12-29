package com.example.exam.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "patients")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;

    private String birthDate;
    private String gender;
    private String phone;
    private String address;
    private String insuranceNumber;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;  // Связь с аккаунтом (роль PATIENT или USER)


}