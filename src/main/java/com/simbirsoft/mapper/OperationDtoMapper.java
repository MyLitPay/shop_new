package com.simbirsoft.mapper;

import com.simbirsoft.api.dto.OperationDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OperationDtoMapper {
    OperationDtoMapper INSTANCE = Mappers.getMapper(OperationDtoMapper.class);

    OperationDto map(OperationDto operationDto);
}
