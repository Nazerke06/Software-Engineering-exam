//package com.example.exam.services;
//
//import com.example.exam.dto.prescriptiondto.PrescriptionRequestDTO;
//import com.example.exam.dto.prescriptiondto.PrescriptionResponseDTO;
//import com.example.exam.entities.*;
//import com.example.exam.mappers.PrescriptionMapper;
//import com.example.exam.repositories.AppointmentRepository;
//import com.example.exam.repositories.PrescriptionRepository;
//import com.example.exam.services.impl.PrescriptionServiceImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.*;
//
//import java.time.LocalDate;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class PrescriptionServiceImplTest {
//
//    @Mock
//    private PrescriptionRepository prescriptionRepository;
//
//    @Mock
//    private AppointmentRepository appointmentRepository;
//
//    @Mock
//    private PrescriptionMapper mapper;
//
//    @InjectMocks
//    private PrescriptionServiceImpl service;
//
//    private DoctorProfile doctor;
//    private Patient patient;
//    private Appointment appointment;
//    private PrescriptionRequestDTO requestDTO;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//
//        // создаём User для доктора
//        User doctorUser = User.builder()
//                .id(1L)
//                .firstName("John")
//                .lastName("Doe")
//                .email("john.doe@example.com")
//                .role(Role.DOCTOR)
//                .build();
//
//        // создаём DoctorProfile
//        doctor = DoctorProfile.builder()
//                .id(10L)
//                .user(doctorUser)
//                .specialization("Cardiologist")
//                .experienceYears(5)
//                .qualification("MD")
//                .build();
//
//        // создаём User для пациента
//        User patientUser = User.builder()
//                .id(2L)
//                .firstName("Jane")
//                .lastName("Smith")
//                .email("jane.smith@example.com")
//                .role(Role.PATIENT)
//                .build();
//
//        // создаём PatientProfile
//        patient = Patient.builder()
//                .id(20L)
//                .user(patientUser)
//                .build();
//
//        // создаём Appointment
//        appointment = Appointment.builder()
//                .id(100L)
//                .doctor(doctor)
//                .patient(patient)
//                .status(Appointment.AppointmentStatus.valueOf("COMPLETED"))
//                .build();
//
//        // DTO запроса
//        requestDTO = PrescriptionRequestDTO.builder()
//                .appointmentId(100L)
//                .description("Take medicine twice")
//                .dosage("2 pills")
//                .medicines("Paracetamol")
//                .prescriptionDate(LocalDate.of(2025, 12, 29))
//                .build();
//    }
//
//    @Test
//    void createPrescriptionSuccess() {
//        Prescription prescriptionEntity = Prescription.builder().build();
//        Prescription savedPrescription = Prescription.builder()
//                .id(500L)
//                .prescriptionDate(LocalDate.of(2025, 12, 29))
//                .description("Take medicine twice")
//                .dosage("2 pills")
//                .medicines("Paracetamol")
//                .user(doctor)
//                .patient(patient)
//                .appointment(appointment)
//                .build();
//        PrescriptionResponseDTO responseDTO = PrescriptionResponseDTO.builder().id(500L).build();
//
//        when(appointmentRepository.findById(100L)).thenReturn(Optional.of(appointment));
//        when(mapper.toEntity(requestDTO)).thenReturn(prescriptionEntity);
//        when(prescriptionRepository.save(any(Prescription.class))).thenReturn(savedPrescription);
//        when(mapper.toDTO(savedPrescription)).thenReturn(responseDTO);
//
//        PrescriptionResponseDTO result = service.createPrescription(requestDTO, doctor);
//
//        assertNotNull(result);
//        assertEquals(500L, result.getId());
//
//        verify(prescriptionRepository, times(1)).save(prescriptionEntity);
//    }
//
//    @Test
//    void createPrescriptionNotDoctor() {
//        User nonDoctor = User.builder().role(Role.PATIENT).build();
//        RuntimeException exception = assertThrows(RuntimeException.class,
//                () -> service.createPrescription(requestDTO, nonDoctor));
//
//        assertEquals("Only DOCTOR can create prescriptions", exception.getMessage());
//    }
//
//    @Test
//    void createPrescriptionAppointmentNotFound() {
//        when(appointmentRepository.findById(100L)).thenReturn(Optional.empty());
//        RuntimeException exception = assertThrows(RuntimeException.class,
//                () -> service.createPrescription(requestDTO, doctor));
//
//        assertEquals("Appointment not found", exception.getMessage());
//    }
//
//    @Test
//    void createPrescriptionAppointmentNotCompleted() {
//        appointment.setStatus(Appointment.AppointmentStatus.valueOf("SCHEDULED"));
//        when(appointmentRepository.findById(100L)).thenReturn(Optional.of(appointment));
//
//        RuntimeException exception = assertThrows(RuntimeException.class,
//                () -> service.createPrescription(requestDTO, doctor));
//
//        assertEquals("Prescription can only be issued after completed appointment", exception.getMessage());
//    }
//
//    @Test
//    void createPrescriptionAppointmentWrongDoctor() {
//        DoctorProfile otherDoctor = DoctorProfile.builder().id(99L).user(
//                User.builder().id(99L).firstName("Other").lastName("Doctor").build()
//        ).build();
//
//        appointment.setDoctor(otherDoctor);
//        when(appointmentRepository.findById(100L)).thenReturn(Optional.of(appointment));
//
//        RuntimeException exception = assertThrows(RuntimeException.class,
//                () -> service.createPrescription(requestDTO, doctor));
//
//        assertEquals("You can only prescribe for your own appointments", exception.getMessage());
//    }
//}
