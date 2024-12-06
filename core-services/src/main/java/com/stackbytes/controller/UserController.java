package com.stackbytes.controller;

import com.stackbytes.model.user.dto.UserCreateRequestDto;
import com.stackbytes.model.user.dto.UserCreateResponseDto;
import com.stackbytes.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    private ResponseEntity<UserCreateResponseDto> createNewUser(@RequestBody UserCreateRequestDto userCreateRequestDto, HttpServletRequest request) {
        UserCreateResponseDto userCreateResponseDto = userService.createNewUser(userCreateRequestDto, request);
        return userCreateResponseDto != null ? ResponseEntity.ok(userCreateResponseDto) : ResponseEntity.internalServerError().build();
    }
}
