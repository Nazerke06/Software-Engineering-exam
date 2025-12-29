package com.example.exam.services.impl;

import com.example.exam.dto.patientdto.PatientRequestDTO;
import com.example.exam.dto.patientdto.PatientResponseDTO;
import com.example.exam.entities.Patient;
import com.example.exam.entities.User;
import com.example.exam.mappers.PatientMapper;
import com.example.exam.repositories.PatientRepository;
import com.example.exam.services.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    @Override
    public PatientResponseDTO createPatientProfile(PatientRequestDTO dto, User currentUser) {
        // Проверяем, есть ли уже профиль для этого пользователя
        patientRepository.findByUser(currentUser).ifPresent(p -> {
            throw new RuntimeException("Patient profile already exists for this user");
        });

        Patient patient = patientMapper.toEntity(dto);
        patient.setUser(currentUser);
        Patient saved = patientRepository.save(patient);
        return patientMapper.toResponseDTO(saved);
    }

    @Override
    public PatientResponseDTO updatePatientProfile(Long id, PatientRequestDTO dto, User currentUser) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        // Разрешаем редактировать только свой профиль
        if (!patient.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You are not allowed to update this profile");
        }

        // Обновляем поля
        patient.setFirstName(dto.getFirstName());
        patient.setLastName(dto.getLastName());
        patient.setBirthDate(dto.getBirthDate());
        patient.setGender(dto.getGender());
        patient.setPhone(dto.getPhone());
        patient.setAddress(dto.getAddress());
        patient.setInsuranceNumber(dto.getInsuranceNumber());

        Patient updated = patientRepository.save(patient);
        return patientMapper.toResponseDTO(updated);
    }

    @Override
    public PatientResponseDTO getPatientById(Long id, User currentUser) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        // Разрешаем смотреть только свой профиль или если админ
        if (!patient.getUser().getId().equals(currentUser.getId()) && !currentUser.getRole().equals("ADMIN")) {
            throw new RuntimeException("You are not allowed to view this profile");
        }

        return patientMapper.toResponseDTO(patient);
    }

    @Override
    public List<PatientResponseDTO> getAllPatients(User currentUser) {
        // Только админ может видеть всех пациентов
        if ((currentUser.getRole().equals("ADMIN"))) {
            throw new RuntimeException("You are not allowed to view all patients");
        }

        return patientRepository.findAll().stream()
                .map(patientMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deletePatientProfile(Long id, User admin) {
        if (!admin.getRole().equals("ADMIN")) {
            throw new RuntimeException("Only admin can delete patient profiles");
        }

        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        patientRepository.delete(patient);
    }
}
