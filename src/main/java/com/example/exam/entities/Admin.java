package com.example.exam.entities;


import jakarta.persistence.Id;
import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Admin {
    @Id
    Long id;
    String adminName;
    Integer adminAge;
    String adminEmail;
    String adminPassword;

}
