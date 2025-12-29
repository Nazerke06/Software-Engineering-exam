package com.example.exam.services;

import com.example.exam.dto.medicinedto.MedicineRequestDTO;
import com.example.exam.dto.medicinedto.MedicineResponseDTO;
import com.example.exam.entities.Medicine;
import com.example.exam.entities.User;
import com.example.exam.mappers.MedicineMapper;
import com.example.exam.repositories.MedicineRepository;
import com.example.exam.services.impl.MedicineServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MedicineServiceTest {

    @Mock
    private MedicineRepository medicineRepository;

    @Mock
    private MedicineMapper medicineMapper;

    @InjectMocks
    private MedicineServiceImpl medicineService;

    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("Test User");
    }

    @Test
    void createMedicine_shouldReturnResponseDTO() {
        MedicineRequestDTO dto = MedicineRequestDTO.builder()
                .name("Aspirin")
                .manufacturer("Pharma")
                .price(10.0)
                .quantityInStock(5)
                .expiryDate("2025-12-31")
                .build();

        Medicine medicineEntity = Medicine.builder().build();
        Medicine savedMedicine = Medicine.builder().id(1L).build();
        MedicineResponseDTO responseDTO = new MedicineResponseDTO();
        responseDTO.setId(1L);

        when(medicineMapper.toEntity(dto)).thenReturn(medicineEntity);
        when(medicineRepository.save(medicineEntity)).thenReturn(savedMedicine);
        when(medicineMapper.toResponseDTO(savedMedicine)).thenReturn(responseDTO);

        MedicineResponseDTO result = medicineService.createMedicine(dto, testUser);

        assertEquals(1L, result.getId());
        verify(medicineRepository).save(medicineEntity);
    }

    @Test
    void getAllMedicines_shouldReturnListOfDTOs() {
        Medicine med1 = Medicine.builder().id(1L).build();
        Medicine med2 = Medicine.builder().id(2L).build();

        MedicineResponseDTO dto1 = new MedicineResponseDTO();
        dto1.setId(1L);
        MedicineResponseDTO dto2 = new MedicineResponseDTO();
        dto2.setId(2L);

        when(medicineRepository.findAll()).thenReturn(Arrays.asList(med1, med2));
        when(medicineMapper.toResponseDTO(med1)).thenReturn(dto1);
        when(medicineMapper.toResponseDTO(med2)).thenReturn(dto2);

        List<MedicineResponseDTO> result = medicineService.getAllMedicines();

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
    }

    @Test
    void deleteMedicine_shouldCallRepositoryDelete() {
        Medicine med = Medicine.builder().id(1L).build();
        when(medicineRepository.findById(1L)).thenReturn(Optional.of(med));

        medicineService.deleteMedicine(1L, testUser);

        verify(medicineRepository).delete(med);
    }

    @Test
    void updateMedicine_shouldReturnUpdatedDTO() {
        MedicineRequestDTO dto = MedicineRequestDTO.builder()
                .name("Updated")
                .manufacturer("Pharma")
                .price(12.0)
                .quantityInStock(10)
                .expiryDate("2025-12-31")
                .build();

        Medicine existingMedicine = Medicine.builder().id(1L).build();
        Medicine updatedMedicine = Medicine.builder().id(1L).build();
        MedicineResponseDTO responseDTO = new MedicineResponseDTO();
        responseDTO.setId(1L);

        when(medicineRepository.findById(1L)).thenReturn(Optional.of(existingMedicine));
        when(medicineRepository.save(existingMedicine)).thenReturn(updatedMedicine);
        when(medicineMapper.toResponseDTO(updatedMedicine)).thenReturn(responseDTO);

        MedicineResponseDTO result = medicineService.updateMedicine(1L, dto, testUser);

        assertEquals(1L, result.getId());
        assertEquals("Updated", existingMedicine.getName());
        verify(medicineRepository).save(existingMedicine);
    }
}
