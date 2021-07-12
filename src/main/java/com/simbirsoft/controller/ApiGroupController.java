package com.simbirsoft.controller;

import com.simbirsoft.api.dto.GroupDto;
import com.simbirsoft.api.response.ResultResponse;
import com.simbirsoft.service.GroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
public class ApiGroupController {

    final GroupService groupService;

    public ApiGroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping
    public List<GroupDto> getAllGroups() {
        return groupService.getAllGroups();
    }

    @PostMapping
    public GroupDto addGroup(@RequestBody GroupDto dto) {
        return groupService.addGroup(dto);
    }

    @PutMapping
    public List<GroupDto> updateAllGroups(@RequestBody List<GroupDto> request) {
        return groupService.updateAllGroups(request);
    }

    @DeleteMapping
    public ResultResponse deleteAllGroups() {
        return groupService.deleteAllGroups();
    }

    @GetMapping("/{id}")
    public GroupDto getGroupById(@PathVariable("id") Long id) {
        return groupService.getGroupById(id);
    }

    @PutMapping("/{id}")
    public GroupDto updateGroupById(@PathVariable("id") Long id,
                                    @RequestBody GroupDto dto) {
        return groupService.updateGroupById(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteGroupById(@PathVariable("id") Long id) {
        groupService.deleteGroupById(id);
    }
}
