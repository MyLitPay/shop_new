package com.simbirsoft.mapper;

import com.simbirsoft.api.dto.ProductDto;
import com.simbirsoft.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mapping(target = "groupId", expression = "java(product.getGroup().getId())")
    ProductDto toDTO(Product product);
    Product toEntity(ProductDto dto);
}
