package com.example.exam.dto.userdto;

import lombok.*;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    private Long id;
    private String username;
    private String fullName;
    private Set<String> roles; // ["ADMIN", "DOCTOR"]
}
