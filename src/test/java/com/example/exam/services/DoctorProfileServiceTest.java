package com.example.exam.services;

import com.example.exam.dto.doctorprofiledto.DoctorProfileRequestDTO;
import com.example.exam.dto.doctorprofiledto.DoctorProfileResponseDTO;
import com.example.exam.entities.DoctorProfile;
import com.example.exam.entities.Role;
import com.example.exam.entities.User;
import com.example.exam.repositories.DoctorProfileRepository;
import com.example.exam.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;
import java.util.Set;

@SpringBootTest
//@Transactional
@ActiveProfiles("test")
public class DoctorProfileServiceTest {

    @Autowired
    private DoctorProfileService doctorProfileService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DoctorProfileRepository doctorProfileRepository;

    private User admin;

    @BeforeEach
    void initData() {
        if (userRepository.findAll().isEmpty()) {
            admin = new User();
            admin.setFullName("Admin");
            admin.setRoles(Set.of(Role.ADMIN));
            admin = userRepository.save(admin);
        } else {
            admin = userRepository.findAll().get(0);
        }
    }

    @Test
    void createProfileTest() {
        User doctor = new User();
        doctor.setFullName("Dr. John Doe");
        doctor.setRoles(Set.of(Role.DOCTOR));
        doctor = userRepository.save(doctor);

        DoctorProfileRequestDTO requestDTO = DoctorProfileRequestDTO.builder()
                .userId(doctor.getId())
                .specialization("Cardiology")
                .experience(10)
                .build();

        DoctorProfileResponseDTO response = doctorProfileService.createProfile(requestDTO, admin);

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getId());
        Assertions.assertEquals("Dr. John Doe", response.getDoctorName());
        Assertions.assertEquals("Cardiology", response.getSpecialization());
        Assertions.assertEquals(10, response.getExperience());

        DoctorProfile savedProfile = doctorProfileRepository.findById(response.getId()).orElse(null);
        Assertions.assertNotNull(savedProfile);
        Assertions.assertEquals("Cardiology", savedProfile.getSpecialization());
        Assertions.assertEquals(10, savedProfile.getExperience());
        Assertions.assertEquals(doctor.getId(), savedProfile.getUser().getId());
    }

    @Test
    void createProfile_userNotFoundTest() {
        DoctorProfileRequestDTO requestDTO = DoctorProfileRequestDTO.builder()
                .userId(-1L)
                .specialization("Neurology")
                .experience(5)
                .build();

        RuntimeException exception = Assertions.assertThrows(
                RuntimeException.class,
                () -> doctorProfileService.createProfile(requestDTO, admin)
        );

        Assertions.assertTrue(exception.getMessage().toLowerCase().contains("not found"));
    }

    @Test
    void deleteProfileTest() {
        User doctor = new User();
        doctor.setFullName("Dr. Delete");
        doctor.setRoles(Set.of(Role.DOCTOR));
        doctor = userRepository.save(doctor);

        DoctorProfile profile = new DoctorProfile();
        profile.setUser(doctor);
        profile.setFullName("Dr. Delete");
        profile.setSpecialization("Surgery");
        profile.setExperience(7);
        profile = doctorProfileRepository.save(profile);

        Long profileId = profile.getId();
        doctorProfileService.deleteProfile(profileId, admin);

        Assertions.assertFalse(doctorProfileRepository.findById(profileId).isPresent());
    }

    @Test
    void getRandomProfileTest() {
        List<DoctorProfile> profiles = doctorProfileRepository.findAll();
        if (profiles.isEmpty()) {
            User doctor = new User();
            doctor.setFullName("Dr. Random");
            doctor.setRoles(Set.of(Role.DOCTOR));
            doctor = userRepository.save(doctor);

            DoctorProfile profile = new DoctorProfile();
            profile.setUser(doctor);
            profile.setFullName("Dr. Random");
            profile.setSpecialization("Dermatology");
            profile.setExperience(3);
            doctorProfileRepository.save(profile);

            profiles = doctorProfileRepository.findAll();
        }

        Random random = new Random();
        DoctorProfile randomProfile = profiles.get(random.nextInt(profiles.size()));

        Assertions.assertNotNull(randomProfile);
        Assertions.assertNotNull(randomProfile.getId());
        Assertions.assertNotNull(randomProfile.getFullName());
        Assertions.assertNotNull(randomProfile.getSpecialization());
    }
}
