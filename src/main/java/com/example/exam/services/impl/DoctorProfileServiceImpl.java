package com.example.exam.services.impl;

import com.example.exam.dto.doctorprofiledto.DoctorProfileRequestDTO;
import com.example.exam.dto.doctorprofiledto.DoctorProfileResponseDTO;
import com.example.exam.entities.DoctorProfile;
import com.example.exam.entities.Role;
import com.example.exam.entities.User;
import com.example.exam.mappers.DoctorProfileMapper;
import com.example.exam.repositories.DoctorProfileRepository;
import com.example.exam.repositories.UserRepository;
import com.example.exam.services.DoctorProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service

public class DoctorProfileServiceImpl implements DoctorProfileService {

    private final DoctorProfileRepository doctorProfileRepository;
    private final UserRepository userRepository;

    @Autowired
    public DoctorProfileServiceImpl(DoctorProfileRepository doctorProfileRepository,
                                    UserRepository userRepository) {
        this.doctorProfileRepository = doctorProfileRepository;
        this.userRepository = userRepository;
    }

    @Override
    public DoctorProfileResponseDTO createProfile(DoctorProfileRequestDTO dto, User admin) {
        if (!admin.getRoles().contains(Role.ADMIN)) {
            throw new RuntimeException("Only admin can create doctor profiles");
        }

        User doctor = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        DoctorProfile profile = new DoctorProfile();
        profile.setUser(doctor);
        profile.setFullName(doctor.getFullName());
        profile.setSpecialization(dto.getSpecialization());
        profile.setExperience(dto.getExperience());

        doctorProfileRepository.save(profile);

        return new DoctorProfileResponseDTO(profile.getId(), profile.getFullName(),
                profile.getSpecialization(), profile.getExperience());
    }

    @Override
    public void deleteProfile(Long id, User admin) {
        if (!admin.getRoles().contains(Role.ADMIN)) {
            throw new RuntimeException("Only admin can delete doctor profiles");
        }

        doctorProfileRepository.deleteById(id);
    }
}
