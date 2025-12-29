package com.example.exam.services.impl;

import com.example.exam.dto.prescriptiondto.PrescriptionRequestDTO;
import com.example.exam.dto.prescriptiondto.PrescriptionResponseDTO;
import com.example.exam.entities.*;
import com.example.exam.mappers.PrescriptionMapper;
import com.example.exam.repositories.AppointmentRepository;
import com.example.exam.repositories.PrescriptionRepository;
import com.example.exam.services.PrescriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class PrescriptionServiceImpl implements PrescriptionService {

    private final PrescriptionRepository repository;
    private final AppointmentRepository appointmentRepository;
    private final PrescriptionMapper mapper;

    @Override
    public PrescriptionResponseDTO createPrescription(PrescriptionRequestDTO dto, User currentUser) {
        if (!currentUser.getRole().equals(Role.DOCTOR)) {
            throw new RuntimeException("Only DOCTOR can create prescriptions");
        }

        Appointment appointment = appointmentRepository.findById(dto.getAppointmentId())
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        if (!appointment.getDoctor().equals(currentUser)) {
            throw new RuntimeException("You can only prescribe for your own appointments");
        }

        if (!"COMPLETED".equals(appointment.getStatus())) {
            throw new RuntimeException("Prescription can only be issued after completed appointment");
        }

        Prescription prescription = mapper.toEntity(dto);
        prescription.setAppointment(appointment);
        prescription.setDoctor(currentUser);
        prescription.setPatient(appointment.getPatient().getUser());
        prescription.setPrescriptionDate(dto.getPrescriptionDate() != null ? dto.getPrescriptionDate() : LocalDate.now());

        Prescription saved = repository.save(prescription);
        return mapper.toDTO(saved);
    }
}