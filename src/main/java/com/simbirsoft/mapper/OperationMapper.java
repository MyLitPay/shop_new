package com.simbirsoft.mapper;

import com.simbirsoft.api.dto.OperationDto;
import com.simbirsoft.model.Operation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OperationMapper {
    OperationMapper INSTANCE = Mappers.getMapper(OperationMapper.class);

    @Mappings({
            @Mapping(target = "prodId", expression = "java(operation.getProduct().getId())"),
            @Mapping(target = "checkId", expression = "java(operation.getCheck().getId())")
    })
    OperationDto toDTO(Operation operation);

    Operation toEntity(OperationDto dto);
}
