package com.example.exam.mappers;

import com.example.exam.entities.DoctorProfile;
import com.example.exam.dto.doctorprofiledto.DoctorProfileRequestDTO;
import com.example.exam.dto.doctorprofiledto.DoctorProfileResponseDTO;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface DoctorProfileMapper {

    @Mapping(source = "fullName", target = "doctorName")
    DoctorProfileResponseDTO toDTO(DoctorProfile entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "fullName", ignore = true)
    DoctorProfile toEntity(DoctorProfileRequestDTO dto);
}