package com.example.exam.dto.reviewdto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequestDTO {
    private Integer rating;     // 1-5
    private String comment;
    private Long appointmentId;
}