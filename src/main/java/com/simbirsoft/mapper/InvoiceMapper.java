package com.simbirsoft.mapper;

import com.simbirsoft.api.dto.InvoiceDto;
import com.simbirsoft.model.Invoice;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface InvoiceMapper {


    InvoiceMapper INSTANCE = Mappers.getMapper(InvoiceMapper.class);

    @Mapping(target = "groupId", expression = "java(invoice.getGroup().getId())")
    InvoiceDto toDTO(Invoice invoice);

    Invoice toEntity(InvoiceDto invoiceDto);
}
