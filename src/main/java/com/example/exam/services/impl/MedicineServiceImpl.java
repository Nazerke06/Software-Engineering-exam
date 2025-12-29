package com.example.exam.services.impl;

import com.example.exam.dto.medicinedto.MedicineRequestDTO;
import com.example.exam.dto.medicinedto.MedicineResponseDTO;
import com.example.exam.entities.Medicine;
import com.example.exam.entities.User;
import com.example.exam.mappers.MedicineMapper;
import com.example.exam.repositories.MedicineRepository;
import com.example.exam.services.MedicineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MedicineServiceImpl implements MedicineService {

    private final MedicineRepository medicineRepository;
    private final MedicineMapper medicineMapper;

    @Override
    public MedicineResponseDTO createMedicine(MedicineRequestDTO dto, User currentUser) {
        // Можно добавить проверку роли пользователя, если только доктор/админ может создавать лекарства
        Medicine medicine = medicineMapper.toEntity(dto);
        Medicine savedMedicine = medicineRepository.save(medicine);
        return medicineMapper.toResponseDTO(savedMedicine);
    }

    @Override
    public MedicineResponseDTO updateMedicine(Long id, MedicineRequestDTO dto, User currentUser) {
        Medicine existingMedicine = medicineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicine not found with id: " + id));

        // Обновляем поля
        existingMedicine.setName(dto.getName());
        existingMedicine.setManufacturer(dto.getManufacturer());
        existingMedicine.setPrice(dto.getPrice());
        existingMedicine.setQuantityInStock(dto.getQuantityInStock());
        existingMedicine.setExpiryDate(dto.getExpiryDate());

        Medicine updatedMedicine = medicineRepository.save(existingMedicine);
        return medicineMapper.toResponseDTO(updatedMedicine);
    }

    @Override
    public List<MedicineResponseDTO> getAllMedicines() {
        return medicineRepository.findAll()
                .stream()
                .map(medicineMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteMedicine(Long id, User currentUser) {
        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicine not found with id: " + id));
        medicineRepository.delete(medicine);
    }
}
