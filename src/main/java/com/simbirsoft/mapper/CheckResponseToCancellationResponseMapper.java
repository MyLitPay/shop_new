package com.simbirsoft.mapper;

import com.simbirsoft.api.response.CancellationResponse;
import com.simbirsoft.api.response.CheckResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CheckResponseToCancellationResponseMapper {
    CheckResponseToCancellationResponseMapper INSTANCE = Mappers.getMapper(CheckResponseToCancellationResponseMapper.class);

    @Mapping(source = "description", target = "reason")
    CancellationResponse toCancellationResponse(CheckResponse checkResponse);
    CheckResponse toCheckResponse(CancellationResponse cancellationResponse);
}
