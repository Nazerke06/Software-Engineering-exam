package com.example.exam.repositories;

import com.example.exam.entities.Appointments;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentsRepository extends JpaRepository<Appointments,Long> {
}
