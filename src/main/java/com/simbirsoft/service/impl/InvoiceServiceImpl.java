package com.simbirsoft.service.impl;

import com.simbirsoft.api.dto.InvoiceDto;
import com.simbirsoft.api.response.ResultResponse;
import com.simbirsoft.api.response.ResultResponseType;
import com.simbirsoft.exception.CancellationNotFoundException;
import com.simbirsoft.exception.GroupNotFoundException;
import com.simbirsoft.exception.InvoiceNotFoundException;
import com.simbirsoft.mapper.InvoiceMapper;
import com.simbirsoft.model.Group;
import com.simbirsoft.model.Invoice;
import com.simbirsoft.repo.GroupRepository;
import com.simbirsoft.repo.InvoiceRepository;
import com.simbirsoft.service.InvoiceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceServiceImpl implements InvoiceService {
    final InvoiceRepository invoiceRepository;
    final GroupRepository groupRepository;

    public InvoiceServiceImpl(InvoiceRepository invoiceRepository, GroupRepository groupRepository) {
        this.invoiceRepository = invoiceRepository;
        this.groupRepository = groupRepository;
    }

    @Override
    public List<InvoiceDto> getAllInvoices() {
        List<InvoiceDto> list = invoiceRepository.findAll().stream()
                .map(InvoiceMapper.INSTANCE::toDTO)
                .collect(Collectors.toList());
        if (list.isEmpty()) {
            throw new InvoiceNotFoundException();
        }
        return list;
    }

    @Override
    public InvoiceDto addInvoice(InvoiceDto invoiceDto) {
        Invoice invoice = InvoiceMapper.INSTANCE.toEntity(invoiceDto);
        Invoice invoiceFromDB = invoiceRepository.saveAndFlush(setConstraints(invoiceDto, invoice));
        return InvoiceMapper.INSTANCE.toDTO(invoiceFromDB);
    }

    @Override
    public List<InvoiceDto> updateAllInvoices(List<InvoiceDto> request) {
        List<Invoice> invoiceList = new ArrayList<>();

        for (InvoiceDto invoiceDto : request) {
            Invoice invoice = updateInvoiceData(findInvoiceById(invoiceDto.getId()), invoiceDto);
            invoiceList.add(invoice);
        }
        invoiceRepository.saveAll(invoiceList);
        return getAllInvoices();
    }

    @Override
    public void deleteAllInvoices() {
        List<Invoice> invoices = invoiceRepository.findAll();
        for (Invoice invoice : invoices) {
            deleteConstraints(invoice);
        }
        invoiceRepository.deleteAll(invoices);
        if (invoices.isEmpty()) {
            throw new InvoiceNotFoundException();
        }
    }

    @Override
    public InvoiceDto getInvoiceById(Long id) {
        Invoice invoice = findInvoiceById(id);
        return InvoiceMapper.INSTANCE.toDTO(invoice);
    }

    @Override
    public InvoiceDto updateInvoiceById(Long id, InvoiceDto invoiceDto) {
        Invoice invoice = updateInvoiceData(findInvoiceById(id), invoiceDto);
        Invoice invoiceFromDB = invoiceRepository.saveAndFlush(invoice);
        return InvoiceMapper.INSTANCE.toDTO(invoiceFromDB);
    }

    @Override
    public void deleteInvoiceById(Long id) {
        deleteConstraints(findInvoiceById(id));
        invoiceRepository.deleteById(id);
    }

    public Invoice findInvoiceById(long id) {
        return invoiceRepository.findById(id)
                .orElseThrow(InvoiceNotFoundException::new);
    }

    private Invoice updateInvoiceData(Invoice invoice, InvoiceDto invoiceDto) {
        invoice.setName(invoiceDto.getName());
        invoice.setPrice(invoiceDto.getPrice());
        invoice.setAmount(invoiceDto.getAmount());
        invoice.setSum(invoiceDto.getSum());

        return setConstraints(invoiceDto, invoice);
    }

    private Invoice setConstraints(InvoiceDto dto, Invoice invoice) {
        if (dto.getGroupId() != null) {
            Group group = getGroupFromDB(dto.getGroupId());
            invoice.setGroup(group);
            if (group.getInvoices() == null) {
                group.setInvoices(new ArrayList<>());
            }
            group.getInvoices().add(invoice);
        }
        return invoice;
    }

    private void deleteConstraints(Invoice invoice) {
        if (invoice.getGroup() != null) {
            Group group = getGroupFromDB(invoice.getGroup().getId());
            group.getInvoices().remove(invoice);
        }
    }

    private Group getGroupFromDB(Long id) {
        return groupRepository.findById(id)
                .orElseThrow(GroupNotFoundException::new);
    }

}
