package com.example.exam.mappers;

import com.example.exam.dto.patientdto.PatientRequestDTO;
import com.example.exam.dto.patientdto.PatientResponseDTO;
import com.example.exam.entities.Patient;
import com.example.exam.entities.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PatientMapperTest {

    private final PatientMapper mapper = new PatientMapperImpl(); // Генерируется MapStruct

    @Test
    void testToResponseDTO() {
        User user = User.builder()
                .id(1L)
                .firstName("user123")
                .build();

        Patient patient = Patient.builder()
                .id(10L)
                .firstName("John")
                .lastName("Doe")
                .birthDate("1990-01-01")
                .gender("Male")
                .phone("1234567890")
                .address("123 Street")
                .insuranceNumber("INS123")
                .user(user)
                .build();

        PatientResponseDTO dto = mapper.toResponseDTO(patient);

        assertEquals("John Doe", dto.getFullName());
        assertEquals(1L, dto.getUserId());
        assertEquals("user123", dto.getUsername());
        assertEquals("1234567890", dto.getPhone());
    }

    @Test
    void testToEntity() {
        PatientRequestDTO dto = PatientRequestDTO.builder()
                .firstName("Jane")
                .lastName("Smith")
                .birthDate("1995-05-05")
                .gender("Female")
                .phone("9876543210")
                .address("456 Avenue")
                .insuranceNumber("INS456")
                .build();

        Patient patient = mapper.toEntity(dto);

        assertNull(patient.getId());  // должно быть игнорировано
        assertEquals("Jane", patient.getFirstName());
        assertEquals("Smith", patient.getLastName());
        assertEquals("9876543210", patient.getPhone());
        assertNull(patient.getUser()); // user игнорируется
    }
}
