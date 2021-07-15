package com.simbirsoft.mapper;

import com.simbirsoft.api.dto.ProductCountDto;
import com.simbirsoft.model.ProductCount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductCountMapper {
    ProductCountMapper INSTANCE = Mappers.getMapper(ProductCountMapper.class);

    @Mapping(target = "prodId", expression = "java(amount.getProduct().getId())")
    ProductCountDto toDTO(ProductCount amount);
    ProductCount toEntity(ProductCountDto dto);
}
