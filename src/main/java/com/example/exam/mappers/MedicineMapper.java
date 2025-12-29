package com.example.exam.mappers;

import com.example.exam.entities.Medicine;
import com.example.exam.dto.medicinedto.MedicineRequestDTO;
import com.example.exam.dto.medicinedto.MedicineResponseDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface MedicineMapper {

    @Mapping(target = "inStock", expression = "java(entity.getQuantityInStock() != null && entity.getQuantityInStock() > 0)")
    MedicineResponseDTO toResponseDTO(Medicine entity);

    @Mapping(target = "id", ignore = true)
    Medicine toEntity(MedicineRequestDTO dto);
}