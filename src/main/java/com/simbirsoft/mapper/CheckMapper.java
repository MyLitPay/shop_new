package com.simbirsoft.mapper;

import com.simbirsoft.api.dto.CheckDto;
import com.simbirsoft.model.Check;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CheckMapper {
    CheckMapper INSTANCE = Mappers.getMapper(CheckMapper.class);

    CheckDto toDTO(Check check);
    Check toEntity(CheckDto dto);
}
