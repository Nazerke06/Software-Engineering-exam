package com.example.exam.services;

import com.example.exam.dto.reviewdto.ReviewRequestDTO;
import com.example.exam.dto.reviewdto.ReviewResponseDTO;
import com.example.exam.entities.Appointment;
import com.example.exam.entities.Review;
import com.example.exam.entities.User;
import com.example.exam.mappers.ReviewMapper;
import com.example.exam.repositories.AppointmentRepository;
import com.example.exam.repositories.ReviewRepository;
import com.example.exam.services.impl.ReviewServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReviewServiceTest {

    private ReviewRepository reviewRepository;
    private AppointmentRepository appointmentRepository;
    private ReviewMapper reviewMapper;
    private ReviewServiceImpl reviewService;

    @BeforeEach
    void setUp() {
        reviewRepository = mock(ReviewRepository.class);
        appointmentRepository = mock(AppointmentRepository.class);
        reviewMapper = new ReviewMapper() {
            @Override
            public ReviewResponseDTO toDTO(Review review) {
                return null;
            }
        }; // анонимная реализация
        reviewService = new ReviewServiceImpl(reviewRepository, appointmentRepository, reviewMapper);
    }

    @Test
    void testCreateReview() {
        User user = User.builder().id(1L).firstName("John").lastName("Doe").build();
        Appointment appointment = Appointment.builder().id(100L).build();
        ReviewRequestDTO dto = ReviewRequestDTO.builder()
                .rating(5)
                .comment("Excellent")
                .appointmentId(100L)
                .build();

        when(appointmentRepository.findById(100L)).thenReturn(Optional.of(appointment));

        ArgumentCaptor<Review> captor = ArgumentCaptor.forClass(Review.class);
        when(reviewRepository.save(captor.capture())).thenAnswer(i -> {
            Review r = i.getArgument(0);
            r.setId(10L); // имитация сохранения с ID
            return r;
        });

        ReviewResponseDTO responseDTO = reviewService.createReview(dto, user);

        Review savedReview = captor.getValue();
        assertEquals(5, savedReview.getRating());
        assertEquals("Excellent", savedReview.getComment());
        assertEquals(user, savedReview.getPatient());
        assertEquals(appointment, savedReview.getAppointment());

        assertEquals(10L, responseDTO.getId());
        assertEquals("John Doe", responseDTO.getPatientFullName());
        assertEquals(100L, responseDTO.getAppointmentId());
    }

    @Test
    void testGetAllReviews() {
        User user = User.builder().id(1L).firstName("Alice").lastName("Smith").build();
        Appointment appointment = Appointment.builder().id(50L).build();
        Review review = Review.builder()
                .id(5L)
                .rating(4)
                .comment("Nice")
                .reviewDate(LocalDate.now())
                .patient(user)
                .appointment(appointment)
                .build();

        when(reviewRepository.findAll()).thenReturn(List.of(review));

        List<ReviewResponseDTO> reviews = reviewService.getAllReviews();
        assertEquals(1, reviews.size());
        assertEquals("Alice Smith", reviews.get(0).getPatientFullName());
    }

    @Test
    void testDeleteReview_Success() {
        User user = User.builder().id(1L).build();
        Review review = Review.builder().id(10L).patient(user).build();
        when(reviewRepository.findById(10L)).thenReturn(Optional.of(review));

        reviewService.deleteReview(10L, user);
        verify(reviewRepository, times(1)).delete(review);
    }

    @Test
    void testDeleteReview_NotAllowed() {
        User user = User.builder().id(1L).build();
        User otherUser = User.builder().id(2L).build();
        Review review = Review.builder().id(10L).patient(otherUser).build();
        when(reviewRepository.findById(10L)).thenReturn(Optional.of(review));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                reviewService.deleteReview(10L, user)
        );

        assertEquals("You are not allowed to delete this review", exception.getMessage());
        verify(reviewRepository, never()).delete(any());
    }
}
