package com.stackbytes.service;

import com.mongodb.client.result.DeleteResult;
import com.stackbytes.model.user.User;
import com.stackbytes.model.user.dto.UserCreateRequestDto;
import com.stackbytes.model.user.dto.UserCreateResponseDto;
import com.stackbytes.utils.CountryUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;

@Service
public class UserService {

    private final MongoTemplate mongoTemplate;
    private final CountryUtils countryUtils;

    public UserService(MongoTemplate mongoTemplate, CountryUtils countryUtils) {
        this.mongoTemplate = mongoTemplate;
        this.countryUtils = countryUtils;
    }

    public UserCreateResponseDto createNewUser(UserCreateRequestDto userCreateRequestDto, HttpServletRequest request) throws UnknownHostException {
        String countryCode = countryUtils.getCountryCode(request);

        System.out.println(countryCode);

        User newUser = User.builder()
                .name(userCreateRequestDto.getName())
                .email(userCreateRequestDto.getEmail())
                .birthday(userCreateRequestDto.getBirthday())
                .interests(userCreateRequestDto.getInterests())
                .groups(new ArrayList<>())
                .events(new ArrayList<>())
                .createdAt(new Date())
                .countryCode(countryCode)
                .build();


        User addedUser = mongoTemplate.insert(newUser);

        return new UserCreateResponseDto(addedUser.getUserId());
    }


    public Boolean deleteUser(String userId){
        Query deleteUserById = Query.query(Criteria.where("_id").is(userId));
        DeleteResult dr =  mongoTemplate.remove(deleteUserById,"users");

        return  dr.getDeletedCount() > 0;
    }
}
