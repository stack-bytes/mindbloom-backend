package com.stackbytes.controller;

import com.stackbytes.model.group.Group;
import com.stackbytes.model.group.dto.CreateGroupRequestDto;
import com.stackbytes.model.group.dto.CreateGroupResponseDto;
import com.stackbytes.service.GroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/groups")
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @CrossOrigin
    @PostMapping
    private ResponseEntity<CreateGroupResponseDto> createGroup(@RequestBody CreateGroupRequestDto createGroupRequestDto) {
        CreateGroupResponseDto createGroupResponseDto = groupService.createNewGroup(createGroupRequestDto);

        return createGroupResponseDto != null ? ResponseEntity.ok(createGroupResponseDto) : ResponseEntity.internalServerError().build();
    }

    @CrossOrigin
    @GetMapping
    private ResponseEntity<Group> getGroup(@RequestParam String groupId) {
        Group g = groupService.getGroupById(groupId);
        return g != null ? ResponseEntity.ok(g) : ResponseEntity.notFound().build();
    }

    @CrossOrigin
    @GetMapping("/user")
    private ResponseEntity<List<Group>> getUserGroups(@RequestParam String userId) {
        List<Group> gs = groupService.getUserGroups(userId);
        return !(gs == null) ? ResponseEntity.ok(gs) : ResponseEntity.notFound().build();
    }

    @CrossOrigin
    @PatchMapping("/user/add") ResponseEntity<Boolean> addUserToGroup(@RequestParam String groupId, @RequestParam String userId) {
        return groupService.addUserToGroup(userId, groupId) ? ResponseEntity.ok(true) : ResponseEntity.notFound().build();
    }

    @CrossOrigin
    @DeleteMapping("/user/remove") ResponseEntity<Boolean> removeUserFromGroup(@RequestParam String groupId, @RequestParam String userId) {
        return  groupService.removeUserFromGroup(userId, groupId) ? ResponseEntity.ok(true) : ResponseEntity.notFound().build();
    }

    @CrossOrigin
    @GetMapping("/therapist")
    private ResponseEntity<List<Group>> getTherapistGroups(@RequestParam String therapistId) {
        List<Group> gs = groupService.getTherapistGroups(therapistId);
        return !(gs == null) ? ResponseEntity.ok(gs) : ResponseEntity.notFound().build();
    }

    @CrossOrigin
    @GetMapping("/interests")
    private ResponseEntity<List<Group>> getInterestGroups(@RequestParam List<String> interests) {
        return ResponseEntity.ok(groupService.getGroupsByInterests(interests));
    }

}
