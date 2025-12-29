package com.example.exam.services.impl;

import com.example.exam.dto.invoicedto.InvoiceRequestDTO;
import com.example.exam.dto.invoicedto.InvoiceResponseDTO;
import com.example.exam.entities.Invoice;
import com.example.exam.entities.User;
import com.example.exam.mappers.InvoiceMapper;
import com.example.exam.repositories.InvoiceRepository;
import com.example.exam.repositories.UserRepository;
import com.example.exam.services.InvoiceService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final UserRepository userRepository; // for patient lookup
    private final InvoiceMapper invoiceMapper;

    @Override
    public InvoiceResponseDTO createInvoice(InvoiceRequestDTO dto, Long patientId) {
        User patient = userRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found with id: " + patientId));

        Invoice invoice = invoiceMapper.toEntity(dto);
        invoice.setPatient(patient);
        // appointment can be set here if needed
        Invoice saved = invoiceRepository.save(invoice);

        return invoiceMapper.toResponseDTO(saved);
    }

    @Override
    public InvoiceResponseDTO getInvoiceById(Long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Invoice not found with id: " + id));

        return invoiceMapper.toResponseDTO(invoice);
    }

    @Override
    public List<InvoiceResponseDTO> getAllInvoices() {
        return invoiceRepository.findAll()
                .stream()
                .map(invoiceMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public InvoiceResponseDTO updateInvoiceStatus(Long id, String status) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Invoice not found with id: " + id));

        invoice.setStatus(status);
        Invoice updated = invoiceRepository.save(invoice);

        return invoiceMapper.toResponseDTO(updated);
    }

    @Override
    public void deleteInvoice(Long id) {
        if (!invoiceRepository.existsById(id)) {
            throw new EntityNotFoundException("Invoice not found with id: " + id);
        }
        invoiceRepository.deleteById(id);
    }
}
