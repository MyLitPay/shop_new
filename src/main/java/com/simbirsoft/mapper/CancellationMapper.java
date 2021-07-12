package com.simbirsoft.mapper;

import com.simbirsoft.api.dto.CancellationDto;
import com.simbirsoft.model.Cancellation;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CancellationMapper {
    CancellationMapper INSTANCE = Mappers.getMapper(CancellationMapper.class);

    CancellationDto toDTO(Cancellation cancellation);
    Cancellation toEntity(CancellationDto dto);
}
