package com.example.exam.mappers;

import com.example.exam.dto.prescriptiondto.PrescriptionRequestDTO;
import com.example.exam.dto.prescriptiondto.PrescriptionResponseDTO;
import com.example.exam.entities.Prescription;
import com.example.exam.entities.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PrescriptionMapperTest {

    private final PrescriptionMapper mapper = new PrescriptionMapperImpl(); // MapStruct generated implementation

    @Test
    void testToDTO() {
        User doctor = User.builder().firstName("John").lastName("Doe").build();
        User patient = User.builder().firstName("Jane").lastName("Smith").build();

        Prescription prescription = Prescription.builder()
                .id(1L)
                .prescriptionDate(LocalDate.of(2025, 12, 29))
                .description("Take medicine twice a day")
                .dosage("2 pills")
                .medicines("Paracetamol")
                .doctor(doctor)
                .patient(patient)
                .build();

        PrescriptionResponseDTO dto = mapper.toDTO(prescription);

        assertEquals(1L, dto.getId());
        assertEquals("John Doe", dto.getDoctorName());
        assertEquals("Jane Smith", dto.getPatientName());
        assertEquals("Take medicine twice a day", dto.getDescription());
        assertEquals("2 pills", dto.getDosage());
        assertEquals("Paracetamol", dto.getMedicines());
    }

    @Test
    void testToEntity() {
        PrescriptionRequestDTO dto = PrescriptionRequestDTO.builder()
                .prescriptionDate(LocalDate.of(2025, 12, 29))
                .description("Take medicine once a day")
                .dosage("1 pill")
                .medicines("Ibuprofen")
                .build();

        Prescription entity = mapper.toEntity(dto);

        assertNull(entity.getId()); // id игнорируется
        assertEquals("Take medicine once a day", entity.getDescription());
        assertEquals("1 pill", entity.getDosage());
        assertEquals("Ibuprofen", entity.getMedicines());
        assertNull(entity.getDoctor());
        assertNull(entity.getPatient());
        assertNull(entity.getAppointment());
    }
}
