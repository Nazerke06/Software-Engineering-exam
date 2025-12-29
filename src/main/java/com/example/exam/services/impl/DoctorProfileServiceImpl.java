package com.example.exam.services.impl;

import com.example.exam.dto.doctorprofiledto.DoctorProfileRequestDTO;
import com.example.exam.dto.doctorprofiledto.DoctorProfileResponseDTO;
import com.example.exam.entities.DoctorProfile;
import com.example.exam.entities.User;
import com.example.exam.mappers.DoctorProfileMapper;
import com.example.exam.repositories.DoctorProfileRepository;
import com.example.exam.repositories.UserRepository;
import com.example.exam.services.DoctorProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional
public class DoctorProfileServiceImpl implements DoctorProfileService {

    private final DoctorProfileRepository doctorProfileRepository;
    private final DoctorProfileMapper doctorProfileMapper;
    private final UserRepository userRepository;

    @Override
    public DoctorProfileResponseDTO createProfile(DoctorProfileRequestDTO dto, User admin) {
        // Проверяем, что пользователь существует
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + dto.getUserId()));

        // Проверяем, что у пользователя ещё нет профиля доктора
        doctorProfileRepository.findByUser(user).ifPresent(dp -> {
            throw new IllegalStateException("Doctor profile already exists for this user");
        });

        // Преобразуем DTO в сущность
        DoctorProfile doctorProfile = doctorProfileMapper.toEntity(dto);
        doctorProfile.setUser(user); // связываем с пользователем

        // Сохраняем профиль
        doctorProfile = doctorProfileRepository.save(doctorProfile);

        // Возвращаем DTO
        return doctorProfileMapper.toDTO(doctorProfile);
    }

    @Override
    public void deleteProfile(Long id, User admin) {
        DoctorProfile doctorProfile = doctorProfileRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Doctor profile not found with id: " + id));

        doctorProfileRepository.delete(doctorProfile);
    }
}
