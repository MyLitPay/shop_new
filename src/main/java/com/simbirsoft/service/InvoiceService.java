package com.simbirsoft.service;

import com.simbirsoft.api.dto.InvoiceDto;
import com.simbirsoft.api.response.ResultResponse;
import com.simbirsoft.model.Invoice;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface InvoiceService {
    ResponseEntity<List<InvoiceDto>> getAllInvoices();
    InvoiceDto addInvoice(InvoiceDto invoiceDto);
    ResponseEntity<List<InvoiceDto>> updateAllInvoices(List<InvoiceDto> request);
    ResultResponse deleteAllInvoices();
    InvoiceDto getInvoiceById(Long id);
    InvoiceDto updateInvoiceById(Long id, InvoiceDto invoiceDto);
    void deleteInvoiceById(Long id);

    Invoice findInvoiceById(long id);
}
