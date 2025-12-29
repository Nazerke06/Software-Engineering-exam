package com.example.exam.services;

import com.example.exam.dto.invoicedto.InvoiceRequestDTO;
import com.example.exam.dto.invoicedto.InvoiceResponseDTO;
import com.example.exam.entities.Invoice;
import com.example.exam.entities.User;
import com.example.exam.mappers.InvoiceMapper;
import com.example.exam.repositories.InvoiceRepository;
import com.example.exam.repositories.UserRepository;
import com.example.exam.services.impl.InvoiceServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InvoiceServiceTest {

    @Mock
    private InvoiceRepository invoiceRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private InvoiceMapper invoiceMapper;

    @InjectMocks
    private InvoiceServiceImpl invoiceService;

    private User patient;
    private Invoice invoice;
    private InvoiceRequestDTO requestDTO;
    private InvoiceResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        patient = User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .build();

        invoice = Invoice.builder()
                .id(1L)
                .amount(100.0)
                .description("Test Invoice")
                .status("PENDING")
                .patient(patient)
                .build();

        requestDTO = InvoiceRequestDTO.builder()
                .amount(100.0)
                .description("Test Invoice")
                .appointmentId(1L)
                .build();

        responseDTO = InvoiceResponseDTO.builder()
                .id(1L)
                .amount(100.0)
                .description("Test Invoice")
                .status("PENDING")
                .patientFullName("John Doe")
                .appointmentId(1L)
                .build();
    }

    @Test
    void createInvoice_shouldReturnResponseDTO() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(invoiceMapper.toEntity(requestDTO)).thenReturn(invoice);
        when(invoiceRepository.save(invoice)).thenReturn(invoice);
        when(invoiceMapper.toResponseDTO(invoice)).thenReturn(responseDTO);

        InvoiceResponseDTO result = invoiceService.createInvoice(requestDTO, 1L);

        assertNotNull(result);
        assertEquals("John Doe", result.getPatientFullName());
        verify(invoiceRepository).save(invoice);
    }

    @Test
    void createInvoice_shouldThrowEntityNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> invoiceService.createInvoice(requestDTO, 1L));
    }

    @Test
    void getInvoiceById_shouldReturnResponseDTO() {
        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));
        when(invoiceMapper.toResponseDTO(invoice)).thenReturn(responseDTO);

        InvoiceResponseDTO result = invoiceService.getInvoiceById(1L);

        assertEquals(1L, result.getId());
        verify(invoiceRepository).findById(1L);
    }

    @Test
    void getInvoiceById_shouldThrowEntityNotFoundException() {
        when(invoiceRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> invoiceService.getInvoiceById(1L));
    }

    @Test
    void getAllInvoices_shouldReturnListOfResponseDTOs() {
        when(invoiceRepository.findAll()).thenReturn(Arrays.asList(invoice));
        when(invoiceMapper.toResponseDTO(invoice)).thenReturn(responseDTO);

        List<InvoiceResponseDTO> result = invoiceService.getAllInvoices();

        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getPatientFullName());
    }

    @Test
    void updateInvoiceStatus_shouldReturnUpdatedDTO() {
        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));
        invoice.setStatus("PAID");
        when(invoiceRepository.save(invoice)).thenReturn(invoice);
        when(invoiceMapper.toResponseDTO(invoice)).thenReturn(responseDTO);

        InvoiceResponseDTO result = invoiceService.updateInvoiceStatus(1L, "PAID");

        assertEquals("John Doe", result.getPatientFullName());
        verify(invoiceRepository).save(invoice);
    }

    @Test
    void updateInvoiceStatus_shouldThrowEntityNotFoundException() {
        when(invoiceRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> invoiceService.updateInvoiceStatus(1L, "PAID"));
    }

    @Test
    void deleteInvoice_shouldCallRepository() {
        when(invoiceRepository.existsById(1L)).thenReturn(true);

        invoiceService.deleteInvoice(1L);

        verify(invoiceRepository).deleteById(1L);
    }

    @Test
    void deleteInvoice_shouldThrowEntityNotFoundException() {
        when(invoiceRepository.existsById(1L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class,
                () -> invoiceService.deleteInvoice(1L));
    }
}
