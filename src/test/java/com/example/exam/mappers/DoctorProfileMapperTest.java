package com.example.exam.mappers;

import com.example.exam.dto.doctorprofiledto.DoctorProfileRequestDTO;
import com.example.exam.dto.doctorprofiledto.DoctorProfileResponseDTO;
import com.example.exam.entities.DoctorProfile;
import com.example.exam.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DoctorProfileMapperTest {

    @Autowired
    private DoctorProfileMapper mapper;

    @Test
    void toDTO_shouldMapFullNameToDoctorName_andOtherFieldsCorrectly() {
        // Given
        DoctorProfile entity = new DoctorProfile();
        entity.setId(1L);
        entity.setFullName("Dr. John Smith");
        entity.setSpecialization("Cardiology");
        entity.setExperience(15);
        entity.setUser(new User()); // user is not mapped, so can be anything

        // When
        DoctorProfileResponseDTO dto = mapper.toDTO(entity);

        // Then
        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Dr. John Smith", dto.getDoctorName());
        assertEquals("Cardiology", dto.getSpecialization());
        assertEquals(Integer.valueOf(15), dto.getExperience());
    }

    @Test
    void toEntity_shouldMapFieldsFromDTO_andIgnoreIdUserAndFullName() {
        // Given
        DoctorProfileRequestDTO requestDTO = DoctorProfileRequestDTO.builder()
                .specialization("Neurology")
                .experience(8)
                .userId(100L)
                .build();

        // When
        DoctorProfile entity = mapper.toEntity(requestDTO);

        // Then
        assertNotNull(entity);
        assertNull(entity.getId());                    // ignored
        assertNull(entity.getUser());                  // ignored
        assertNull(entity.getFullName());              // explicitly ignored

        assertEquals("Neurology", entity.getSpecialization());
        assertEquals(Integer.valueOf(8), entity.getExperience());
    }

    @Test
    void toDTO_shouldHandleNullEntity() {
        // When
        DoctorProfileResponseDTO dto = mapper.toDTO(null);

        // Then
        assertNull(dto);
    }

    @Test
    void toEntity_shouldHandleNullDTO() {
        // When
        DoctorProfile entity = mapper.toEntity(null);

        // Then
        assertNull(entity);
    }
}