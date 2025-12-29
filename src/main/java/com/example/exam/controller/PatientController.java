package com.example.exam.controller;

import com.example.exam.dto.patientdto.PatientRequestDTO;
import com.example.exam.dto.patientdto.PatientResponseDTO;
import com.example.exam.entities.User;
import com.example.exam.services.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('PATIENT')")
    public ResponseEntity<PatientResponseDTO> createPatient(
            @Valid @RequestBody PatientRequestDTO dto,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.status(201).body(patientService.createPatientProfile(dto, currentUser));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PATIENT')")
    public ResponseEntity<PatientResponseDTO> updatePatient(
            @PathVariable Long id,
            @Valid @RequestBody PatientRequestDTO dto,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(patientService.updatePatientProfile(id, dto, currentUser));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR') or hasRole('PATIENT')")
    public ResponseEntity<PatientResponseDTO> getPatientById(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(patientService.getPatientById(id, currentUser));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PatientResponseDTO>> getAllPatients(
            @AuthenticationPrincipal User admin) {
        return ResponseEntity.ok(patientService.getAllPatients(admin));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePatient(
            @PathVariable Long id,
            @AuthenticationPrincipal User admin) {
        patientService.deletePatientProfile(id, admin);
        return ResponseEntity.noContent().build();
    }
}