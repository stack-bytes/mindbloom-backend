package com.stackbytes.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.result.UpdateResult;
import com.stackbytes.model.group.Group;
import com.stackbytes.model.group.dto.CreateGroupRequestDto;
import com.stackbytes.model.group.dto.CreateGroupResponseDto;
import com.stackbytes.model.user.User;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GroupService {

    private final MongoTemplate mongoTemplate;
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    private final String GROUP_PREFIX = "group";

    public GroupService(MongoTemplate mongoTemplate, RedisTemplate<String, String> redisTemplate, ObjectMapper objectMapper) {
        this.mongoTemplate = mongoTemplate;
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    public CreateGroupResponseDto createNewGroup(CreateGroupRequestDto createGroupRequestDto) {
        Group g = Group.builder()
                .name(createGroupRequestDto.getName())
                .description(createGroupRequestDto.getDescription())
                .created_at(new Date())
                .updated_at(new Date())
                .owner(createGroupRequestDto.getOwner())
                .members(createGroupRequestDto.getMembers() == null ? new ArrayList<>() : createGroupRequestDto.getMembers())
                .avatarUrl("")
                .coordinate_x(createGroupRequestDto.getCoordinate_x())
                .coordinate_y(createGroupRequestDto.getCoordinate_y())
                .location(createGroupRequestDto.getLocation())
                .metadata(createGroupRequestDto.getMetadata())
                .build();


        Group ret = mongoTemplate.insert(g);

        List<String> info = createGroupRequestDto.getMetadata().getInterests();
        info.addAll(createGroupRequestDto.getMetadata().getTags());

        info.forEach((e)->{
            redisTemplate.opsForValue().append(String.format("%s:%s", GROUP_PREFIX, e), g.getId());
        });





        return new CreateGroupResponseDto(ret.getId());
    }

    public Group getGroupById(String groupId) {
        return mongoTemplate.findById(groupId, Group.class);
    }

    public List<Group> getUserGroups(String userId) {
        Query query = new Query(Criteria.where("members").in(userId));

        return mongoTemplate.find(query, Group.class);
    }

    public List<Group> getTherapistGroups(String therapistId) {
        List<Group> gs = mongoTemplate.find(new Query(Criteria.where("owner").is(therapistId)), Group.class);

        return gs.isEmpty() ? null : gs;
    }

    public boolean addUserToGroup(String userId, String groupId) {
        Query query = new Query(Criteria.where("_id").is(groupId));
        Update update = new Update().addToSet("members", userId);

        UpdateResult ur =  mongoTemplate.updateFirst(query, update, Group.class);

        if(ur.getModifiedCount() == 0)
            return false;

        query = new Query(Criteria.where("_id").is(userId));
        update = new Update().addToSet("groups", groupId);

        ur =  mongoTemplate.updateFirst(query, update, User.class);

        return ur.getModifiedCount() > 0;
    }

    public boolean removeUserFromGroup(String userId, String groupId) {
        Query query = new Query(Criteria.where("_id").is(groupId));
        Update update = new Update().pull("members", userId);

        UpdateResult ur =  mongoTemplate.updateFirst(query, update, Group.class);

        if(ur.getModifiedCount() == 0)
            return false;

        query = new Query(Criteria.where("_id").is(userId));
        update = new Update().pull("groups", groupId);
        ur =  mongoTemplate.updateFirst(query, update, User.class);

        return ur.getModifiedCount() > 0;
    }

    public List<Group> getGroupsByInterests(List<String> interests) {

        //Set solves for dupplicationx
        Set<String> groupIds = interests.stream()
                .map((i)->{
                    String groupId = redisTemplate.opsForValue().get(String.format("%s:%s", GROUP_PREFIX, i));
                    System.out.println(groupId);
                    return groupId;
                })
                .collect(Collectors.toSet());


        //MONOG Fallback /\

        List<Group> groups = groupIds.stream().map((id)->{
            return mongoTemplate.findOne(new Query(Criteria.where("_id").is(id)), Group.class);
        }).toList();

        return groups;
    }

    public List<Group> getAllGroups() {
        List<Group> groups = mongoTemplate.findAll(Group.class);
        return groups;
    }
}
