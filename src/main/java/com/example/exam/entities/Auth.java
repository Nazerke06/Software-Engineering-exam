package com.example.exam.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Auth {
    @Id
    private Long id;
    private String username;
    private String password;
}