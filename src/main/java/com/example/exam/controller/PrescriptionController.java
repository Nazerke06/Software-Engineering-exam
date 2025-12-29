package com.example.exam.controller;

import com.example.exam.dto.prescriptiondto.PrescriptionRequestDTO;
import com.example.exam.dto.prescriptiondto.PrescriptionResponseDTO;
import com.example.exam.entities.User;
import com.example.exam.services.PrescriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/prescriptions")
@RequiredArgsConstructor
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    @PostMapping
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<PrescriptionResponseDTO> createPrescription(
            @Valid @RequestBody PrescriptionRequestDTO dto,
            @AuthenticationPrincipal User doctor) {
        return ResponseEntity.status(201).body(prescriptionService.createPrescription(dto, doctor));
    }
}