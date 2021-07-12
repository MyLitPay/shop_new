package com.simbirsoft.controller;

import com.simbirsoft.api.dto.InvoiceDto;
import com.simbirsoft.api.response.ResultResponse;
import com.simbirsoft.service.InvoiceService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
public class ApiInvoiceController {
    final InvoiceService invoiceService;

    public ApiInvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping
    public List<InvoiceDto> getAllInvoices() {
        return invoiceService.getAllInvoices();
    }

    @PostMapping
    public InvoiceDto addInvoice(@RequestBody InvoiceDto invoiceDto) {
        return invoiceService.addInvoice(invoiceDto);
    }

    @PutMapping
    public List<InvoiceDto> updateAllInvoices(@RequestBody List<InvoiceDto> request) {
        return invoiceService.updateAllInvoices(request);
    }

    @DeleteMapping
    public ResultResponse deleteAllInvoices() {
        return invoiceService.deleteAllInvoices();
    }

    @GetMapping("/{id}")
    public InvoiceDto getInvoiceById(@PathVariable("id") Long id) {
        return invoiceService.getInvoiceById(id);
    }

    @PutMapping("/{id}")
    public InvoiceDto updateInvoiceById(@PathVariable("id") Long id,
                                     @RequestBody InvoiceDto invoiceDto) {
        return invoiceService.updateInvoiceById(id, invoiceDto);
    }

    @DeleteMapping("/{id}")
    public ResultResponse deleteInvoiceById(@PathVariable("id") Long id) {
        return invoiceService.deleteInvoiceById(id);
    }
}
