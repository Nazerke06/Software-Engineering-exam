package com.example.exam.repositories;

import com.example.exam.entities.DoctorProfile;
import com.example.exam.entities.Patient;
import com.example.exam.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoctorProfileRepository extends JpaRepository<DoctorProfile, Long> {
    Optional<DoctorProfile> findByUser(User user);
}
