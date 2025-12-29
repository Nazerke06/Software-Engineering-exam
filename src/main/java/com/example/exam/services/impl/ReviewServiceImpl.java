package com.example.exam.services.impl;

import com.example.exam.dto.reviewdto.ReviewRequestDTO;
import com.example.exam.dto.reviewdto.ReviewResponseDTO;
import com.example.exam.entities.Appointment;
import com.example.exam.entities.Review;
import com.example.exam.entities.User;
import com.example.exam.mappers.ReviewMapper;
import com.example.exam.repositories.AppointmentRepository;
import com.example.exam.repositories.ReviewRepository;
import com.example.exam.services.ReviewService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final AppointmentRepository appointmentRepository;
    private final ReviewMapper reviewMapper;

    public ReviewServiceImpl(ReviewRepository reviewRepository,
                             AppointmentRepository appointmentRepository,
                             ReviewMapper reviewMapper) {
        this.reviewRepository = reviewRepository;
        this.appointmentRepository = appointmentRepository;
        this.reviewMapper = reviewMapper;
    }

    @Override
    public ReviewResponseDTO createReview(ReviewRequestDTO dto, User currentUser) {
        // Найти запись о приеме
        Appointment appointment = appointmentRepository.findById(dto.getAppointmentId())
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + dto.getAppointmentId()));

        // Создать отзыв
        Review review = reviewMapper.toEntity(dto);
        review.setAppointment(appointment);
        review.setPatient(currentUser);
        review.setReviewDate(LocalDate.now());

        Review savedReview = reviewRepository.save(review);

        // Преобразовать в ResponseDTO
        return ReviewResponseDTO.builder()
                .id(savedReview.getId())
                .rating(savedReview.getRating())
                .comment(savedReview.getComment())
                .reviewDate(savedReview.getReviewDate())
                .patientFullName(currentUser.getFirstName() + " " + currentUser.getLastName())
                .appointmentId(appointment.getId())
                .build();
    }

    @Override
    public List<ReviewResponseDTO> getAllReviews() {
        return reviewRepository.findAll().stream()
                .map(review -> ReviewResponseDTO.builder()
                        .id(review.getId())
                        .rating(review.getRating())
                        .comment(review.getComment())
                        .reviewDate(review.getReviewDate())
                        .patientFullName(review.getPatient().getFirstName() + " " + review.getPatient().getLastName())
                        .appointmentId(review.getAppointment().getId())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public void deleteReview(Long id, User currentUser) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found with id: " + id));

        // Можно удалять только свой отзыв
        if (!review.getPatient().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You are not allowed to delete this review");
        }

        reviewRepository.delete(review);
    }
}
