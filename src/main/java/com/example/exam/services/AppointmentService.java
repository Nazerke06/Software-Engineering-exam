package com.example.exam.services;

import com.example.exam.dto.appointmentdto.AppointmentRequestDTO;
import com.example.exam.dto.appointmentdto.AppointmentResponseDTO;
import com.example.exam.entities.User;

import java.util.List;


public interface AppointmentService {
    AppointmentResponseDTO createAppointment(AppointmentRequestDTO dto, User patient);
    AppointmentResponseDTO cancelAppointment(Long id, User admin);
    List<AppointmentResponseDTO> getAllAppointments();
}
