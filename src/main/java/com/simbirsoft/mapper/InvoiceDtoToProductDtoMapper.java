package com.simbirsoft.mapper;

import com.simbirsoft.api.dto.InvoiceDto;
import com.simbirsoft.api.dto.ProductAmountDto;
import com.simbirsoft.api.dto.ProductDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface InvoiceDtoToProductDtoMapper {
    InvoiceDtoToProductDtoMapper INSTANCE = Mappers.getMapper(InvoiceDtoToProductDtoMapper.class);

    ProductDto toProductDto(InvoiceDto invoiceDtoDto);
    ProductAmountDto toProductAmountDto(InvoiceDto invoiceDto);

}
