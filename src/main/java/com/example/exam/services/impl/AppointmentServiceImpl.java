package com.example.exam.services.impl;

import com.example.exam.dto.appointmentdto.AppointmentRequestDTO;
import com.example.exam.dto.appointmentdto.AppointmentResponseDTO;
import com.example.exam.entities.Appointment;
import com.example.exam.entities.Role;
import com.example.exam.entities.User;
import com.example.exam.mappers.AppointmentMapper;
import com.example.exam.repositories.AppointmentRepository;
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

    @Override
    public AppointmentResponseDTO createAppointment(AppointmentRequestDTO dto, User patient) {
        // Проверяем, что пациент имеет роль PATIENT (или USER, если у вас пациенты — это USER)
        if (!patient.getRoles().contains(Role.PATIENT)) {
            throw new RuntimeException("Only patients can create appointments");
        }

        Appointment appointment = appointmentMapper.toEntity(dto);
        appointment.setPatient(patient);

        // Находим доктора по ID
        User doctor = userRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        // Опционально: проверка, что найденный пользователь — доктор
        if (!doctor.getRoles().contains(Role.DOCTOR)) {
            throw new RuntimeException("Selected user is not a doctor");
        }

        appointment.setDoctor(doctor);
        appointment.setStatus("SCHEDULED"); // или "CREATED" — выберите одно

        Appointment saved = appointmentRepository.save(appointment);
        return appointmentMapper.toResponseDTO(saved); // исправлено: toResponseDTO
    }

    @Override
    public AppointmentResponseDTO cancelAppointment(Long id, User currentUser) {
        // Можно разрешить отмену и пациенту, и админу — или только админу
        boolean isAdmin = currentUser.getRoles().contains(Role.ADMIN);
        boolean isPatient = currentUser.getRoles().contains(Role.PATIENT);

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        // Пример: отменить может админ или пациент, которому принадлежит запись
        if (!isAdmin && !(isPatient && appointment.getPatient().equals(currentUser))) {
            throw new RuntimeException("You don't have permission to cancel this appointment");
        }

        appointment.setStatus("CANCELED");
        Appointment saved = appointmentRepository.save(appointment);
        return appointmentMapper.toResponseDTO(saved);
    }

    @Override
    public List<AppointmentResponseDTO> getAllAppointments() {
        return appointmentRepository.findAll()
                .stream()
                .map(appointmentMapper::toResponseDTO)
                .toList();
    }

    // Опционально: добавить метод для получения записей конкретного пациента или доктора
}
