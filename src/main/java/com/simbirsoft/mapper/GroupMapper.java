package com.simbirsoft.mapper;

import com.simbirsoft.api.dto.GroupDto;
import com.simbirsoft.model.Group;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface GroupMapper {
    GroupMapper INSTANCE = Mappers.getMapper(GroupMapper.class);

    GroupDto toDTO(Group group);
    Group toEntity(GroupDto dto);
}
