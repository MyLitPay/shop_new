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
            @Mapping(target = "checkId", expression = "java(operation.getCheck().getId())"),
            @Mapping(target = "cancellationId", expression = "java(operation.getCancellation().getId())")
//            @Mapping(target = "operation", source = "operation", qualifiedByName = "enumToString")
    })
    OperationDto toDTO(Operation operation);

    Operation toEntity(OperationDto dto);

//    @Named("enumToString")
//    static String enumToString(Operation operation) {
//        return operation.getOperation().name();
//    }
}
