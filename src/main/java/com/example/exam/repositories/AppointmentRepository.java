package com.example.exam.repositories;

import com.example.exam.entities.Appointment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment,Long> {
}
