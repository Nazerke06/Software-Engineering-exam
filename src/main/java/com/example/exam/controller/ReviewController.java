package com.example.exam.controller;

import com.example.exam.dto.reviewdto.ReviewRequestDTO;
import com.example.exam.dto.patientdto.PatientResponseDTO;
import com.example.exam.dto.reviewdto.ReviewResponseDTO;
import com.example.exam.entities.User;
import com.example.exam.services.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    @PreAuthorize("hasRole('PATIENT')")  // или hasRole('USER'), если пациенты имеют роль PATIENT
    public ResponseEntity<ReviewResponseDTO> createReview(
            @Valid @RequestBody ReviewRequestDTO dto,
            @AuthenticationPrincipal User patient) {
        return ResponseEntity.status(201).body(reviewService.createReview(dto, patient));
    }

    @GetMapping
    public ResponseEntity<List<ReviewResponseDTO>> getAllReviews() {
        return ResponseEntity.ok(reviewService.getAllReviews());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteReview(
            @PathVariable Long id,
            @AuthenticationPrincipal User admin) {
        reviewService.deleteReview(id, admin);
        return ResponseEntity.noContent().build();
    }
}