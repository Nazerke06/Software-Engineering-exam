package com.example.exam.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "medicines")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Medicine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String manufacturer;
    private Double price;
    private Integer quantityInStock;
    private String expiryDate; // или LocalDate
}