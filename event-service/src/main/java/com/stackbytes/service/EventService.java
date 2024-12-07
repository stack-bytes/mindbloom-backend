package com.stackbytes.service;

import com.mongodb.client.result.UpdateResult;
import com.stackbytes.model.Event;
import com.stackbytes.model.dto.EventCreateRequestDto;
import com.stackbytes.model.dto.EventCreateResponseDto;
import com.stackbytes.model.dto.MapEventResponseDto;
import com.stackbytes.model.ref.EventParticipantRef;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.yaml.snakeyaml.util.Tuple;

import java.util.ArrayList;
import java.util.List;

@Service
public class EventService {

    private final MongoTemplate mongoTemplate;
    private final RabbitTemplate rabbitTemplate;

    private final String monolithUsersEventsApi = "http://localhost:8080/users/events";

    private final RestTemplate restTemplate;
    @Autowired
    public EventService(MongoTemplate mongoTemplate, RabbitTemplate rabbitTemplate, RestTemplate restTemplate) {
        this.mongoTemplate = mongoTemplate;
        this.rabbitTemplate = rabbitTemplate;
        this.restTemplate = restTemplate;
    }

    public EventCreateResponseDto createEvent(EventCreateRequestDto eventCreateRequestDto) {
        Event newEvent = Event.builder()
                .name(eventCreateRequestDto.getName())
                .description(eventCreateRequestDto.getDescription())
                .time(eventCreateRequestDto.getTime())
                .groupId(eventCreateRequestDto.getGroupId())
                .location(eventCreateRequestDto.getLocation())
                .eventParticipantRefs(new ArrayList<>())
                .participants(0)
                .coordinate_x(eventCreateRequestDto.getCoordinate_x())
                .coordinate_y(eventCreateRequestDto.getCoordinate_y())
                .build();

        //Cache

        Event createdEvent = mongoTemplate.insert(newEvent);


        return new EventCreateResponseDto(createdEvent.getId());
    }



    public List<MapEventResponseDto> getEventsFromGroup(String groupId) {
        System.out.println(groupId);
        Query findEventsByGroupId = Query.query(Criteria.where("groupId").is(groupId));
        List<Event> events = mongoTemplate.find(findEventsByGroupId, Event.class);


        for(Event event : events) {
            System.out.println(event.getGroupId());
        }
        return events.stream().map(event -> {
            MapEventResponseDto mapEventResponseDtoTemp = MapEventResponseDto.builder()
                    .id(event.getId())
                    .participants(event.getParticipants())
                    .time(event.getTime())
                    .name(event.getName())
                    .coordinate_x(event.getCoordinate_x())
                    .coordinate_y(event.getCoordinate_y())
                    .location(event.getLocation())
                    .description(event.getDescription())
                    .build();
            return mapEventResponseDtoTemp;
        }).toList();


    }


    public Tuple<Boolean, EventParticipantRef> addUserToEvent(String eventId, String userId) {
        EventParticipantRef epr = null;
        try{
            epr = restTemplate.postForObject(String.format("%s?userId=%s&eventId=%s", monolithUsersEventsApi, userId, eventId), null, EventParticipantRef.class);
        } catch (Exception e){
            return new Tuple<>(false, null);
        }


       if(epr == null) {
           return new Tuple<>(false, null);
       }

       Event e = mongoTemplate.findOne(Query.query(Criteria.where("_id").is(eventId)), Event.class);

       Query query = Query.query(Criteria.where("_id").is(eventId));
       Update update = new Update().inc("participants", 1);

       UpdateResult ur = mongoTemplate.updateFirst(query, update, Event.class);

       if(ur.getModifiedCount() == 0){
           return new Tuple<>(false, null);
       }

      update = new Update().addToSet("eventParticipantRefs", epr);

       mongoTemplate.updateFirst(query, update, Event.class);

       return new Tuple<>(true, null);
    }
}
