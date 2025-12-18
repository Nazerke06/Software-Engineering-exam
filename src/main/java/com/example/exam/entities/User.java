package com.example.exam.entities;

import jakarta.persistence.Id;
import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Users {
    @Id
    Long id;
    String userName;
    String userEmail;
    String userPhone;

}
