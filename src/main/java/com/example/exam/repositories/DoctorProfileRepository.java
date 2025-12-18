package com.example.exam.repositories;

import com.example.exam.entities.DoctorProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorsRepositories extends JpaRepository<DoctorProfile, Long> {
}
