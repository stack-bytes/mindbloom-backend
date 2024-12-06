package com.stackbytes.service;

import com.stackbytes.model.user.User;
import com.stackbytes.model.user.dto.UserCreateRequestDto;
import com.stackbytes.model.user.dto.UserCreateResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserService {

    private final MongoTemplate mongoTemplate;

    public UserService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public UserCreateResponseDto createNewUser(UserCreateRequestDto userCreateRequestDto, HttpServletRequest request) {
        User newUser = User.builder()
                .name(userCreateRequestDto.getName())
                .email(userCreateRequestDto.getEmail())
                .birthday(userCreateRequestDto.getBirthday())
                .interests(userCreateRequestDto.getInterests())
                .createdAt(new Date())
                .countryCode(null)
                .build();


        User addedUser = mongoTemplate.insert(newUser);

        return new UserCreateResponseDto(addedUser.getUserId());
    }
}
