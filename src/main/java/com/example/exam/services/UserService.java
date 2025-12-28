package com.example.exam.services;

import com.example.exam.dto.userdto.UserRequestDTO;
import com.example.exam.dto.userdto.UserResponseDTO;
import com.example.exam.entities.User;

import java.util.List;

public interface UserService {
    UserResponseDTO createUser(UserRequestDTO dto, User admin);
    List<UserResponseDTO> getAllUsers();
    UserResponseDTO getUserById(Long id);
    void deleteUser(Long id, User admin);
}
