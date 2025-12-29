package com.example.exam.services.impl;

import com.example.exam.dto.appointmentdto.AppointmentRequestDTO;
import com.example.exam.dto.appointmentdto.AppointmentResponseDTO;
import com.example.exam.entities.*;
import com.example.exam.mappers.AppointmentMapper;
import com.example.exam.repositories.AppointmentRepository;
import com.example.exam.repositories.DoctorProfileRepository;
import com.example.exam.repositories.PatientRepository;
import com.example.exam.repositories.UserRepository;
import com.example.exam.services.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


    @Service
    @RequiredArgsConstructor
    public class AppointmentServiceImpl implements AppointmentService {

        private final AppointmentRepository appointmentRepository;
        private final AppointmentMapper appointmentMapper;
        private final UserRepository userRepository;
        private final PatientRepository patientRepository; // добавили
        private final DoctorProfileRepository doctorProfileRepository;

        @Override
        public AppointmentResponseDTO createAppointment(AppointmentRequestDTO dto, User patientUser) {
            if (patientUser.getRole() != Role.PATIENT) {
                throw new RuntimeException("Only patients can create appointments");
            }

            Patient patient = patientRepository.findByUser(patientUser)
                    .orElseThrow(() -> new RuntimeException("Patient profile not found"));

            Appointment appointment = appointmentMapper.toEntity(dto);
            appointment.setPatient(patient);

            User doctorUser = userRepository.findById(dto.getDoctorId())
                    .orElseThrow(() -> new RuntimeException("Doctor not found"));

            if (doctorUser.getRole() != Role.DOCTOR) {
                throw new RuntimeException("Selected user is not a doctor");
            }

            DoctorProfile doctorProfile = doctorProfileRepository.findByUser(doctorUser)
                    .orElseThrow(() -> new RuntimeException("Doctor profile not found"));

            appointment.setDoctor(doctorProfile);
            appointment.setStatus(Appointment.AppointmentStatus.BOOKED);

            Appointment saved = appointmentRepository.save(appointment);
            return appointmentMapper.toDTO(saved);
        }


        @Override
    public AppointmentResponseDTO cancelAppointment(Long id, User currentUser) {
        // Можно разрешить отмену и пациенту, и админу — или только админу
        boolean isAdmin = currentUser.getRole().equals(Role.ADMIN);
        boolean isPatient = currentUser.getRole().equals(Role.PATIENT);

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        // Пример: отменить может админ или пациент, которому принадлежит запись
        if (!isAdmin && !(isPatient && appointment.getPatient().equals(currentUser))) {
            throw new RuntimeException("You don't have permission to cancel this appointment");
        }

        appointment.setStatus(Appointment.AppointmentStatus.valueOf("CANCELED"));
        Appointment saved = appointmentRepository.save(appointment);
        return appointmentMapper.toDTO(saved);
    }

    @Override
    public List<AppointmentResponseDTO> getAllAppointments() {
        return appointmentRepository.findAll()
                .stream()
                .map(appointmentMapper::toDTO)
                .toList();
    }

    // Опционально: добавить метод для получения записей конкретного пациента или доктора
}
