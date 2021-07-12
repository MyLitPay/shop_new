package com.simbirsoft.mapper;

import com.simbirsoft.api.dto.ProductAmountDto;
import com.simbirsoft.model.ProductAmount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductAmountMapper {
    ProductAmountMapper INSTANCE = Mappers.getMapper(ProductAmountMapper.class);

    @Mapping(target = "prodId", expression = "java(amount.getProduct().getId())")
    ProductAmountDto toDTO(ProductAmount amount);
    ProductAmount toEntity(ProductAmountDto dto);
}
