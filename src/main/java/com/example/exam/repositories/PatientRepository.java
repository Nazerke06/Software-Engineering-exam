package com.example.exam.repositories;

import com.example.exam.entities.Patient;
import com.example.exam.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByUser(User user);
}