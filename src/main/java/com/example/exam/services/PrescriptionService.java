package com.example.exam.services;

import com.example.exam.dto.prescriptiondto.PrescriptionRequestDTO;
import com.example.exam.dto.prescriptiondto.PrescriptionResponseDTO;
import com.example.exam.entities.User;

public interface PrescriptionService {
    PrescriptionResponseDTO createPrescription(PrescriptionRequestDTO dto, User currentUser);
}