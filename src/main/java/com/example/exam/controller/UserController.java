package com.example.exam.controller;

import com.example.exam.dto.userdto.UserRequestDTO;
import com.example.exam.dto.userdto.UserResponseDTO;
import com.example.exam.entities.User;
import com.example.exam.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> createUser(
            @Valid @RequestBody UserRequestDTO dto,
            @AuthenticationPrincipal User admin) {
        UserResponseDTO response = userService.createUser(dto, admin);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id") // Админ или сам пользователь
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long id,
            @AuthenticationPrincipal User admin) {
        userService.deleteUser(id, admin);
        return ResponseEntity.noContent().build();
    }
}