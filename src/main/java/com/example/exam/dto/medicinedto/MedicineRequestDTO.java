package com.example.exam.dto.medicinedto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicineRequestDTO {
    private String name;
    private String manufacturer;
    private Double price;
    private Integer quantityInStock;
    private String expiryDate;
}