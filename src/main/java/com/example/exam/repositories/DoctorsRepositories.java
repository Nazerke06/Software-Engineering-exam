package com.example.exam.repositories;

import com.example.exam.entities.Doctors;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorsRepositories extends JpaRepository<Doctors, Long> {
}
