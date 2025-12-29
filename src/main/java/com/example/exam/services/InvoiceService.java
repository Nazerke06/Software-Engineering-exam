package com.example.exam.services;

import com.example.exam.dto.invoicedto.InvoiceRequestDTO;
import com.example.exam.dto.invoicedto.InvoiceResponseDTO;

import java.util.List;

public interface InvoiceService {

    InvoiceResponseDTO createInvoice(InvoiceRequestDTO invoiceRequestDTO, Long patientId);

    InvoiceResponseDTO getInvoiceById(Long id);

    List<InvoiceResponseDTO> getAllInvoices();

    InvoiceResponseDTO updateInvoiceStatus(Long id, String status);

    void deleteInvoice(Long id);
}
