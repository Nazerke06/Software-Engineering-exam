package com.example.exam.services;

import com.example.exam.dto.medicalrecorddto.MedicalRecordRequestDTO;
import com.example.exam.dto.medicalrecorddto.MedicalRecordResponseDTO;

public interface MedicalRecordService {
    MedicalRecordResponseDTO createRecord(MedicalRecordRequestDTO dto);
}
