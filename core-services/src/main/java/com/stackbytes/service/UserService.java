package com.stackbytes.service;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.stackbytes.model.user.User;
import com.stackbytes.model.user.dto.EventParticipantRefResponseDto;
import com.stackbytes.model.user.dto.UserCreateRequestDto;
import com.stackbytes.model.user.dto.UserCreateResponseDto;
import com.stackbytes.utils.CountryUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.ResponseEntity;
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
                .pfpUrl("") //Will be uploaded through rabbit
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

    public EventParticipantRefResponseDto addUserEvent(String userId, String eventId) {
        Query query = Query.query(Criteria.where("_id").is(userId));


        Update update = new Update();
        update.addToSet("events", eventId);

        UpdateResult ur =  mongoTemplate.updateFirst(query, update, "users");
        System.out.println(ur.getModifiedCount());

        if (ur.getModifiedCount() == 0)
            return null;

        User user = mongoTemplate.findOne(query, User.class, "users");

        if(user == null)
            return null;

        EventParticipantRefResponseDto responseDto = EventParticipantRefResponseDto.builder()
                .id(userId)
                .name(user.getName())
                .pfpUrl(user.getPfpUrl())
                .build();

        return responseDto;
    }

    public boolean removeEventFromUser(String userId, String eventId) {
        Query query = Query.query(Criteria.where("_id").is(userId));
        Update rem = new Update();
        rem.pull("events", eventId);
        UpdateResult ur =  mongoTemplate.updateFirst(query, rem, "users");

        return ur.getModifiedCount() > 0;
    }

    public User getFullUser(String userId) {
        User u = mongoTemplate.findOne(Query.query(Criteria.where("_id").is(userId)), User.class);
        return u;
    }
}
