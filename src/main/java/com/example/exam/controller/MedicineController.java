package com.example.exam.controller;

import com.example.exam.dto.medicinedto.MedicineRequestDTO;
import com.example.exam.dto.medicinedto.MedicineResponseDTO;
import com.example.exam.entities.User;
import com.example.exam.services.MedicineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicines")
@RequiredArgsConstructor
public class MedicineController {

    private final MedicineService medicineService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')")
    public ResponseEntity<MedicineResponseDTO> create(
            @Valid @RequestBody MedicineRequestDTO dto,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.status(201).body(medicineService.createMedicine(dto, user));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')")
    public ResponseEntity<MedicineResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody MedicineRequestDTO dto,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(medicineService.updateMedicine(id, dto, user));
    }

    @GetMapping
    public ResponseEntity<List<MedicineResponseDTO>> getAll() {
        return ResponseEntity.ok(medicineService.getAllMedicines());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        medicineService.deleteMedicine(id, user);
        return ResponseEntity.noContent().build();
    }
}