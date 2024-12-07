package com.stackbytes.service;

import com.mongodb.client.result.UpdateResult;
import com.stackbytes.model.group.Group;
import com.stackbytes.model.group.dto.CreateGroupRequestDto;
import com.stackbytes.model.group.dto.CreateGroupResponseDto;
import com.stackbytes.model.user.User;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class GroupService {

    private final MongoTemplate mongoTemplate;

    public GroupService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public CreateGroupResponseDto createNewGroup(CreateGroupRequestDto createGroupRequestDto) {
        Group g = Group.builder()
                .name(createGroupRequestDto.getName())
                .description(createGroupRequestDto.getDescription())
                .created_at(new Date())
                .updated_at(new Date())
                .owner(createGroupRequestDto.getOwner())
                .members(createGroupRequestDto.getMemebers() == null ? new ArrayList<>() : createGroupRequestDto.getMemebers())
                .avatarUrl("")
                .coordinate_x(createGroupRequestDto.getCoordinate_x())
                .coordinate_y(createGroupRequestDto.getCoordinate_y())
                .location(createGroupRequestDto.getLocation())
                .metadata(createGroupRequestDto.getMetadata())
                .build();


        Group ret = mongoTemplate.insert(g);

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
}
