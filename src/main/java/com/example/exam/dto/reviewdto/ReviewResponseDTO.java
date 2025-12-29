package com.example.exam.dto.reviewdto;

import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponseDTO {
    private Long id;
    private Integer rating;
    private String comment;
    private LocalDate reviewDate;
    private String patientFullName;
    private Long appointmentId;
}