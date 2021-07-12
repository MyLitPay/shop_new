package com.simbirsoft.service.impl;

import com.simbirsoft.api.dto.GroupDto;
import com.simbirsoft.api.response.ResultResponse;
import com.simbirsoft.api.response.ResultResponseType;
import com.simbirsoft.exception.GroupNotFoundException;
import com.simbirsoft.mapper.GroupMapper;
import com.simbirsoft.model.Group;
import com.simbirsoft.repo.GroupRepository;
import com.simbirsoft.service.GroupService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupServiceImpl implements GroupService {
    final GroupRepository groupRepository;

    public GroupServiceImpl(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Override
    public List<GroupDto> getAllGroups() {
        return groupRepository.findAll().stream()
                .map(GroupMapper.INSTANCE::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public GroupDto addGroup(GroupDto dto) {
        Group group = GroupMapper.INSTANCE.toEntity(dto);
        Group groupFromDB = groupRepository.saveAndFlush(group);
        return GroupMapper.INSTANCE.toDTO(groupFromDB);
    }

    @Override
    public List<GroupDto> updateAllGroups(List<GroupDto> request) {
        List<Group> groupList = new ArrayList<>();

        for (GroupDto dto : request) {
            Group group = updateGroupData(findGroupById(dto.getId()), dto);
            groupList.add(group);
        }
        groupRepository.saveAll(groupList);
        return getAllGroups();
    }

    @Override
    public ResultResponse deleteAllGroups() {
        try {
            List<Group> groups = groupRepository.findAll();
            groupRepository.deleteAll(groups);
            return new ResultResponse(ResultResponseType.OK);
        } catch (Exception ex) {
            return new ResultResponse(ResultResponseType.ERROR);
        }
    }

    @Override
    public GroupDto getGroupById(Long id) {
        Group group = findGroupById(id);
        return GroupMapper.INSTANCE.toDTO(group);
    }

    @Override
    public GroupDto updateGroupById(Long id, GroupDto dto) {
        Group group = updateGroupData(findGroupById(id), dto);
        Group groupFromDB = groupRepository.saveAndFlush(group);
        return GroupMapper.INSTANCE.toDTO(groupFromDB);
    }

    @Override
    public ResultResponse deleteGroupById(Long id) {
        try {
            groupRepository.deleteById(id);
            return new ResultResponse(ResultResponseType.OK);
        } catch (Exception ex) {
            return new ResultResponse(ResultResponseType.ERROR);
        }
    }

    public Group findGroupById(long id) {
        return groupRepository.findById(id)
                .orElseThrow(() -> new GroupNotFoundException("Group not found"));
    }

    private Group updateGroupData(Group group, GroupDto dto) {
        group.setName(dto.getName());
        return group;
    }
}
