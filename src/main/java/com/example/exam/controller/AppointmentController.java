package com.example.exam.controller;


import com.example.exam.dto.appointmentdto.AppointmentRequestDTO;
import com.example.exam.dto.appointmentdto.AppointmentResponseDTO;
import com.example.exam.entities.User;
import com.example.exam.services.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping
    @PreAuthorize("hasRole('USER')") // Пациент (роль USER)
    public ResponseEntity<AppointmentResponseDTO> createAppointment(
            @Valid @RequestBody AppointmentRequestDTO dto,
            @AuthenticationPrincipal User patient) {
        AppointmentResponseDTO response = appointmentService.createAppointment(dto, patient);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AppointmentResponseDTO> cancelAppointment(
            @PathVariable Long id,
            @AuthenticationPrincipal User admin) {
        AppointmentResponseDTO response = appointmentService.cancelAppointment(id, admin);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')") // Только админ видит все
    public ResponseEntity<List<AppointmentResponseDTO>> getAllAppointments() {
        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }
}