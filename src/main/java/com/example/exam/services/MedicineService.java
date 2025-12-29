package com.example.exam.services;

import com.example.exam.dto.medicinedto.MedicineRequestDTO;
import com.example.exam.dto.medicinedto.MedicineResponseDTO;
import com.example.exam.entities.User;

import java.util.List;

public interface MedicineService {
    MedicineResponseDTO createMedicine(MedicineRequestDTO dto, User currentUser);
    MedicineResponseDTO updateMedicine(Long id, MedicineRequestDTO dto, User currentUser);
    List<MedicineResponseDTO> getAllMedicines();
    void deleteMedicine(Long id, User currentUser);
}