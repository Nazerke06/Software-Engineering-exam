 package com.example.exam.services;

import com.example.exam.dto.doctorprofiledto.DoctorProfileRequestDTO;
import com.example.exam.dto.doctorprofiledto.DoctorProfileResponseDTO;
import com.example.exam.entities.User;


 public interface DoctorProfileService {
     DoctorProfileResponseDTO createProfile(DoctorProfileRequestDTO dto, User admin);
     void deleteProfile(Long id, User admin);
 }
