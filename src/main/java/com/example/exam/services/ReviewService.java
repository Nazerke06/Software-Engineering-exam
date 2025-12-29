package com.example.exam.services;

import com.example.exam.dto.reviewdto.ReviewRequestDTO;
import com.example.exam.dto.reviewdto.ReviewResponseDTO;
import com.example.exam.entities.User;

import java.util.List;

public interface ReviewService {
    ReviewResponseDTO createReview(ReviewRequestDTO dto, User currentUser);
    List<ReviewResponseDTO> getAllReviews();
    void deleteReview(Long id, User currentUser);
}