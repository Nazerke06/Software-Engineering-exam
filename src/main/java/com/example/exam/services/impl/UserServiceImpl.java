package com.example.exam.services.impl;

import com.example.exam.dto.userdto.UserRequestDTO;
import com.example.exam.dto.userdto.UserResponseDTO;
import com.example.exam.entities.Role;
import com.example.exam.entities.User;
import com.example.exam.mappers.UserMapper;
import com.example.exam.repositories.UserRepository;
import com.example.exam.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    private UserService self;

    @Autowired
    public void setSelf(@Autowired @Lazy UserService self) {
        this.self = self;
    }

    @Override
    public UserResponseDTO createUser(UserRequestDTO dto, User admin) {
        if (!admin.getRoles().contains(Role.ADMIN)) {
            throw new RuntimeException("Only admin can create users");
        }

        User user = userMapper.toEntity(dto);
        if (dto.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        user.setEnabled(true);

        User saved = userRepository.save(user);
        return userMapper.toDTO(saved);
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDTO)
                .toList();
    }

    @Override
    public UserResponseDTO getUserById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public void deleteUser(Long id, User admin) {
        if (!admin.getRoles().contains(Role.ADMIN)) {
            throw new RuntimeException("Only admin can delete users");
        }

        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }

        userRepository.deleteById(id);
    }
}
