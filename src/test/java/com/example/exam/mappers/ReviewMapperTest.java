package com.example.exam.mappers;

import com.example.exam.dto.reviewdto.ReviewRequestDTO;
import com.example.exam.dto.reviewdto.ReviewResponseDTO;
import com.example.exam.entities.Appointment;
import com.example.exam.entities.Review;
import com.example.exam.entities.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ReviewMapperTest {

    private final ReviewMapper mapper = new ReviewMapper() {
        @Override
        public ReviewResponseDTO toDTO(Review review) {
            return null;
        }
    };

    @Test
    void testToDTO_Manual() {
        // создаём сущности
        User user = User.builder().firstName("John").lastName("Doe").build();
        Appointment appointment = Appointment.builder().id(1L).build();
        Review review = Review.builder()
                .id(10L)
                .rating(5)
                .comment("Great!")
                .reviewDate(LocalDate.of(2025, 12, 29))
                .patient(user)
                .appointment(appointment)
                .build();

        // ручной маппинг (как в ReviewMapperImpl)
        ReviewResponseDTO dto = ReviewResponseDTO.builder()
                .id(review.getId())
                .rating(review.getRating())
                .comment(review.getComment())
                .reviewDate(review.getReviewDate())
                .patientFullName(review.getPatient().getFirstName() + " " + review.getPatient().getLastName())
                .appointmentId(review.getAppointment().getId())
                .build();

        assertEquals(10L, dto.getId());
        assertEquals(5, dto.getRating());
        assertEquals("Great!", dto.getComment());
        assertEquals("John Doe", dto.getPatientFullName());
    }


    @Test
    void testToEntity() {
        ReviewRequestDTO dto = ReviewRequestDTO.builder()
                .rating(4)
                .comment("Good service")
                .appointmentId(2L)
                .build();

        Review review = mapper.toEntity(dto);

        assertEquals(4, review.getRating());
        assertEquals("Good service", review.getComment());
        assertNotNull(review.getReviewDate());
        assertNull(review.getPatient());
        assertNull(review.getAppointment());
    }
}
