package com.simbirsoft.mapper;

import com.simbirsoft.api.dto.ProductForCheckDto;
import com.simbirsoft.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductToProductForCheckDtoMapper {
    ProductToProductForCheckDtoMapper INSTANCE = Mappers.getMapper(ProductToProductForCheckDtoMapper.class);

    @Mapping(source = "name", target = "productName")
    ProductForCheckDto toProductForCheckDto(Product product);
    Product toProduct(ProductForCheckDto dto);
}
