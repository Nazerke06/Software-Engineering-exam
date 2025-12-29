package com.example.exam.mappers;

import com.example.exam.entities.DoctorProfile;
import com.example.exam.dto.doctorprofiledto.DoctorProfileRequestDTO;
import com.example.exam.dto.doctorprofiledto.DoctorProfileResponseDTO;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface DoctorProfileMapper {

    @Mapping(
            target = "doctorName",
            expression = "java(entity.getUser().getFirstName() + \" \" + entity.getUser().getLastName())"
    )
    @Mapping(target = "experience", source = "experienceYears") // добавляем это
    DoctorProfileResponseDTO toDTO(DoctorProfile entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "experienceYears", source = "experience") // добавляем это
    DoctorProfile toEntity(DoctorProfileRequestDTO dto);
}
