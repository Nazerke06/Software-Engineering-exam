package com.example.exam.mappers;

import com.example.exam.dto.medicinedto.MedicineRequestDTO;
import com.example.exam.dto.medicinedto.MedicineResponseDTO;
import com.example.exam.entities.Medicine;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MedicineMapperTest {

    private final MedicineMapper mapper = new MedicineMapperImpl(); // MapStruct сгенерирует эту реализацию

    @Test
    void toResponseDTO_shouldMapInStockCorrectly() {
        Medicine medicine = Medicine.builder()
                .id(1L)
                .name("Aspirin")
                .manufacturer("Pharma")
                .price(10.0)
                .quantityInStock(5)
                .expiryDate("2025-12-31")
                .build();

        MedicineResponseDTO response = mapper.toResponseDTO(medicine);

        assertEquals(1L, response.getId());
        assertEquals("Aspirin", response.getName());
        assertTrue(response.isInStock());
    }

    @Test
    void toEntity_shouldIgnoreId() {
        MedicineRequestDTO dto = MedicineRequestDTO.builder()
                .name("Ibuprofen")
                .manufacturer("Pharma")
                .price(15.0)
                .quantityInStock(0)
                .expiryDate("2026-06-30")
                .build();

        Medicine entity = mapper.toEntity(dto);

        assertNull(entity.getId());
        assertEquals("Ibuprofen", entity.getName());
        assertEquals(0, entity.getQuantityInStock());
    }
}
