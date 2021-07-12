package com.simbirsoft.service;

import com.simbirsoft.api.dto.InvoiceDto;
import com.simbirsoft.api.response.ResultResponse;
import com.simbirsoft.model.Invoice;

import java.util.List;

public interface InvoiceService {
    List<InvoiceDto> getAllInvoices();
    InvoiceDto addInvoice(InvoiceDto invoiceDto);
    List<InvoiceDto> updateAllInvoices(List<InvoiceDto> request);
    ResultResponse deleteAllInvoices();
    InvoiceDto getInvoiceById(Long id);
    InvoiceDto updateInvoiceById(Long id, InvoiceDto invoiceDto);
    ResultResponse deleteInvoiceById(Long id);

    Invoice findInvoiceById(long id);
}
