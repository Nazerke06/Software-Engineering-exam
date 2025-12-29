package com.example.exam.mappers;

import com.example.exam.dto.doctorprofiledto.DoctorProfileRequestDTO;
import com.example.exam.dto.doctorprofiledto.DoctorProfileResponseDTO;
import com.example.exam.entities.DoctorProfile;
import com.example.exam.entities.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class DoctorProfileMapperTest {

    private final DoctorProfileMapper mapper = Mappers.getMapper(DoctorProfileMapper.class);

    @Test
    void toDTO_mappingCorrect() {
        User user = User.builder().firstName("John").lastName("Doe").build();
        DoctorProfile profile = DoctorProfile.builder()
                .id(1L)
                .user(user)
                .specialization("Cardiologist")
                .experienceYears(10)
                .build();

        DoctorProfileResponseDTO dto = mapper.toDTO(profile);

        assertEquals(1L, dto.getId());
        assertEquals("John Doe", dto.getDoctorName());
        assertEquals("Cardiologist", dto.getSpecialization());
        assertEquals(10, dto.getExperience());
    }

    @Test
    void toEntity_mappingCorrect() {
        DoctorProfileRequestDTO dto = DoctorProfileRequestDTO.builder()
                .specialization("Cardiologist")
                .experience(10)
                .build();

        DoctorProfile entity = mapper.toEntity(dto);

        assertNull(entity.getId());
        assertEquals("Cardiologist", entity.getSpecialization());
        assertEquals(10, entity.getExperienceYears());
    }
}
