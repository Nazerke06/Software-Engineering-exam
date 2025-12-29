package com.example.exam.services;

import com.example.exam.dto.patientdto.PatientRequestDTO;
import com.example.exam.dto.patientdto.PatientResponseDTO;
import com.example.exam.entities.User;

import java.util.List;

public interface PatientService {
    PatientResponseDTO createPatientProfile(PatientRequestDTO dto, User currentUser);
    PatientResponseDTO updatePatientProfile(Long id, PatientRequestDTO dto, User currentUser);
    PatientResponseDTO getPatientById(Long id, User currentUser);
    List<PatientResponseDTO> getAllPatients(User currentUser);
    void deletePatientProfile(Long id, User admin);
}