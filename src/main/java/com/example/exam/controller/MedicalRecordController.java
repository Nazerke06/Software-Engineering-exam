package com.example.exam.controller;

import com.example.exam.dto.medicalrecorddto.MedicalRecordRequestDTO;
import com.example.exam.dto.medicalrecorddto.MedicalRecordResponseDTO;
import com.example.exam.services.MedicalRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/medical-records")
@RequiredArgsConstructor
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    @PostMapping
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<MedicalRecordResponseDTO> createRecord(
            @Valid @RequestBody MedicalRecordRequestDTO dto) {
        MedicalRecordResponseDTO response = medicalRecordService.createRecord(dto);
        return ResponseEntity.status(201).body(response);
    }
}