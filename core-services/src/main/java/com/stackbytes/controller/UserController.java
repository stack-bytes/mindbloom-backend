package com.stackbytes.controller;

import com.stackbytes.model.user.dto.UserCreateRequestDto;
import com.stackbytes.model.user.dto.UserCreateResponseDto;
import com.stackbytes.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
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

    @PostMapping
    private ResponseEntity<UserCreateResponseDto> createNewUser(@RequestBody UserCreateRequestDto userCreateRequestDto, HttpServletRequest request) throws UnknownHostException {
        UserCreateResponseDto userCreateResponseDto = userService.createNewUser(userCreateRequestDto, request);
        return userCreateResponseDto != null ? ResponseEntity.ok(userCreateResponseDto) : ResponseEntity.internalServerError().build();
    }

    @DeleteMapping
    private ResponseEntity<Boolean> deleteUser(@RequestParam String userId){
        return userService.deleteUser(userId) ? ResponseEntity.ok(true) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
    }
}
