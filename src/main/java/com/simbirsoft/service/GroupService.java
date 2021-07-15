package com.simbirsoft.service;

import com.simbirsoft.api.dto.GroupDto;
import com.simbirsoft.api.response.ResultResponse;
import com.simbirsoft.model.Group;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface GroupService {
    List<GroupDto> getAllGroups();
    GroupDto addGroup(GroupDto dto);
    List<GroupDto> updateAllGroups(List<GroupDto> request);
    void deleteAllGroups();
    GroupDto getGroupById(Long id);
    GroupDto updateGroupById(Long id, GroupDto dto);
    void deleteGroupById(Long id);

    Group findGroupById(long id);
}
