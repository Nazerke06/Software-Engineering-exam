package com.example.exam.repositories;

import com.example.exam.entities.DoctorProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorProfileRepository extends JpaRepository<DoctorProfile, Long> {
}
