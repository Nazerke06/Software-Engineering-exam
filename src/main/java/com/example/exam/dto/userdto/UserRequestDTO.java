package com.example.exam.dto.userdto;

import lombok.*;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {
    private String username;
    private String password;
    private String fullName;
    private Set<String> roles; // ["ADMIN", "DOCTOR"]
}