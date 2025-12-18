package com.example.exam.repositories;

import com.example.exam.entities.Appointment;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentsRepository extends JpaRepository<Appointment,Long> {
}
