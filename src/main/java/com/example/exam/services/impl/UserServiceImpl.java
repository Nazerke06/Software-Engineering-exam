package com.example.exam.services.impl;

import com.example.exam.dto.userdto.UserRequestDTO;
import com.example.exam.dto.userdto.UserResponseDTO;
import com.example.exam.entities.Role;
import com.example.exam.entities.User;
import com.example.exam.mappers.UserMapper;
import com.example.exam.repositories.UserRepository;
import com.example.exam.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDTO createUser(UserRequestDTO dto, User admin) {
        // Проверяем, что админ создает пользователей
        if (admin.getRole() != Role.ADMIN) {
            throw new RuntimeException("Only admins can create users");
        }

        User user = userMapper.toEntity(dto);
        // Хэшируем пароль
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        // Сохраняем пользователя
        user = userRepository.save(user);
        return userMapper.toDTO(user);
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return userMapper.toDTO(user);
    }

    @Override
    public void deleteUser(Long id, User admin) {
        if (admin.getRole() != Role.ADMIN) {
            throw new RuntimeException("Only admins can delete users");
        }
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }
}
