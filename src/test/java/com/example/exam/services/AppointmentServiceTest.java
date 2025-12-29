package com.example.exam.services;

import com.example.exam.dto.appointmentdto.AppointmentRequestDTO;
import com.example.exam.dto.appointmentdto.AppointmentResponseDTO;
import com.example.exam.entities.*;
import com.example.exam.repositories.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
public class AppointmentServiceTest {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorProfileRepository doctorProfileRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    private User doctorUser;
    private User patientUser;
    private DoctorProfile doctorProfile;
    private Patient patientProfile;

    @BeforeEach
    void setup() {
        // Clear all tables in proper order
        appointmentRepository.deleteAll();
        patientRepository.deleteAll();
        doctorProfileRepository.deleteAll();
        userRepository.deleteAll();

        // 1. Save doctor user
        doctorUser = new User();
        doctorUser.setUsername("Dr. Sarah Connor");
        doctorUser.setRole(Role.DOCTOR);
        doctorUser = userRepository.saveAndFlush(doctorUser); // <--- flush to DB

        // 2. Save doctor profile referencing persisted user
        doctorProfile = new DoctorProfile();
        doctorProfile.setUser(doctorUser);
        doctorProfile.setSpecialization("Cardiology");
        doctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile); // <--- flush

        // 3. Save patient user
        patientUser = new User();
        patientUser.setUsername("John Doe");
        patientUser.setRole(Role.PATIENT);
        patientUser = userRepository.saveAndFlush(patientUser); // <--- flush

        // 4. Save patient profile referencing persisted user
        patientProfile = new Patient();
        patientProfile.setUser(patientUser);
        patientProfile = patientRepository.saveAndFlush(patientProfile); // <--- flush
    }

    @Test
    void createAppointmentTest() {
        AppointmentRequestDTO requestDTO = AppointmentRequestDTO.builder()
                .doctorId(doctorUser.getId())
                .appointmentDate(LocalDateTime.of(2025, 12, 30, 14, 30))
                .reason("Regular check-up")
                .build();

        AppointmentResponseDTO response = appointmentService.createAppointment(requestDTO, patientUser);

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getId());
        Assertions.assertEquals("Dr. Sarah Connor", response.getDoctorName());
        Assertions.assertEquals("John Doe", response.getPatientName());
        Assertions.assertEquals(Appointment.AppointmentStatus.BOOKED.name(), response.getStatus());
        Assertions.assertEquals("Regular check-up", response.getReason());

        Appointment saved = appointmentRepository.findById(response.getId()).orElse(null);
        Assertions.assertNotNull(saved);
        Assertions.assertEquals(patientProfile.getId(), saved.getPatient().getId());
        Assertions.assertEquals(doctorProfile.getId(), saved.getDoctor().getId());
    }

    @Test
    void cancelAppointmentTest() {
        // Create appointment
        Appointment appointment = new Appointment();
        appointment.setDoctor(doctorProfile);
        appointment.setPatient(patientProfile);
        appointment.setAppointmentDate(LocalDateTime.now().plusDays(1));
        appointment.setDescription("Test cancel");
        appointment.setStatus(Appointment.AppointmentStatus.BOOKED);
        appointment = appointmentRepository.save(appointment);

        // Cancel appointment
        AppointmentResponseDTO response = appointmentService.cancelAppointment(appointment.getId(), patientUser);

        Assertions.assertEquals(Appointment.AppointmentStatus.CANCELLED.name(), response.getStatus());

        Appointment canceled = appointmentRepository.findById(appointment.getId()).orElseThrow();
        Assertions.assertEquals(Appointment.AppointmentStatus.CANCELLED, canceled.getStatus());
    }

    @Test
    void getAllAppointmentsTest() {
        // Create appointment
        Appointment appointment = new Appointment();
        appointment.setDoctor(doctorProfile);
        appointment.setPatient(patientProfile);
        appointment.setAppointmentDate(LocalDateTime.now().plusDays(2));
        appointment.setDescription("Checkup");
        appointment.setStatus(Appointment.AppointmentStatus.BOOKED);
        appointmentRepository.save(appointment);

        List<AppointmentResponseDTO> appointments = appointmentService.getAllAppointments();

        Assertions.assertFalse(appointments.isEmpty());
        Assertions.assertTrue(appointments.stream().anyMatch(a -> a.getPatientName().equals("John Doe")));
    }
}
