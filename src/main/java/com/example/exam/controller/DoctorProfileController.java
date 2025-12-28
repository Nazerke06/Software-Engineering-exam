package com.example.exam.controller;

import com.example.exam.dto.doctorprofiledto.DoctorProfileRequestDTO;
import com.example.exam.dto.doctorprofiledto.DoctorProfileResponseDTO;
import com.example.exam.entities.User;
import com.example.exam.services.DoctorProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/doctor-profiles")
@RequiredArgsConstructor
public class DoctorProfileController {

    private final DoctorProfileService doctorProfileService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DoctorProfileResponseDTO> createProfile(
            @Valid @RequestBody DoctorProfileRequestDTO dto,
            @AuthenticationPrincipal User admin) {
        DoctorProfileResponseDTO response = doctorProfileService.createProfile(dto, admin);
        return ResponseEntity.status(201).body(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProfile(
            @PathVariable Long id,
            @AuthenticationPrincipal User admin) {
        doctorProfileService.deleteProfile(id, admin);
        return ResponseEntity.noContent().build();
    }
}