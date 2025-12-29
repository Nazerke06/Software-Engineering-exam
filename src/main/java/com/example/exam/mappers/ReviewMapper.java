package com.example.exam.mappers;

import com.example.exam.dto.reviewdto.ReviewResponseDTO;
import com.example.exam.dto.reviewdto.ReviewRequestDTO;
import com.example.exam.entities.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mapping(target = "patientFullName", expression = "java(review.getPatient().getFirstName() + \" \" + review.getPatient().getLastName())")
    @Mapping(target = "appointmentId", expression = "java(review.getAppointment().getId())")
    ReviewResponseDTO toDTO(Review review);

    // Конвертация из ReviewRequestDTO в Review делается вручную через default метод
    default Review toEntity(ReviewRequestDTO dto) {
        Review review = new Review();
        review.setRating(dto.getRating());
        review.setComment(dto.getComment());
        review.setReviewDate(java.time.LocalDate.now());
        return review;
    }
}
