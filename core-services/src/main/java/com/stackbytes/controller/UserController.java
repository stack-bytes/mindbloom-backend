package com.stackbytes.controller;

import com.stackbytes.model.user.dto.EventParticipantRefResponseDto;
import com.stackbytes.model.user.dto.UserCreateRequestDto;
import com.stackbytes.model.user.dto.UserCreateResponseDto;
import com.stackbytes.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @CrossOrigin
    @PostMapping
    private ResponseEntity<UserCreateResponseDto> createNewUser(@RequestBody UserCreateRequestDto userCreateRequestDto, HttpServletRequest request) throws UnknownHostException {
        UserCreateResponseDto userCreateResponseDto = userService.createNewUser(userCreateRequestDto, request);
        return userCreateResponseDto != null ? ResponseEntity.ok(userCreateResponseDto) : ResponseEntity.internalServerError().build();
    }
    @CrossOrigin
    @DeleteMapping
    private ResponseEntity<Boolean> deleteUser(@RequestParam String userId){
        return userService.deleteUser(userId) ? ResponseEntity.ok(true) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
    }


    /* Inter Service */
    @CrossOrigin
    @PostMapping("/events")
    private ResponseEntity<EventParticipantRefResponseDto> addUserToEvent(@RequestParam String userId, @RequestParam String eventId){
        EventParticipantRefResponseDto eventParticipantRefResponseDto =  userService.addUserEvent(userId, eventId);
        return eventParticipantRefResponseDto != null ? ResponseEntity.ok(eventParticipantRefResponseDto) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @CrossOrigin
    @DeleteMapping("/events")
    private ResponseEntity<Null> removeUserFromEvent(@RequestParam String userId, @RequestParam String eventId){
        return userService.removeEventFromUser(userId, eventId) ? ResponseEntity.ok(null) : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @CrossOrigin
    @GetMapping
    private ResponseEntity<String> sanity(){
        return ResponseEntity.ok("Users endpoint working!");
    }
}
