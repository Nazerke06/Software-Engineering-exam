package com.example.exam.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
public enum Role {
    ADMIN,
    DOCTOR,
    PATIENT
}