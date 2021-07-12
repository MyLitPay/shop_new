package com.simbirsoft.service;

import com.simbirsoft.api.dto.GroupDto;
import com.simbirsoft.api.response.ResultResponse;
import com.simbirsoft.model.Group;

import java.util.List;

public interface GroupService {
    List<GroupDto> getAllGroups();
    GroupDto addGroup(GroupDto dto);
    List<GroupDto> updateAllGroups(List<GroupDto> request);
    ResultResponse deleteAllGroups();
    GroupDto getGroupById(Long id);
    GroupDto updateGroupById(Long id, GroupDto dto);
    ResultResponse deleteGroupById(Long id);

    Group findGroupById(long id);
}
