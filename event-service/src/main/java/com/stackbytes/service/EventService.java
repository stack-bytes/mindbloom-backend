package com.stackbytes.service;

import com.stackbytes.model.Event;
import com.stackbytes.model.dto.EventCreateRequestDto;
import com.stackbytes.model.dto.EventCreateResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.util.Tuple;

@Service
public class EventService {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public EventService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public EventCreateResponseDto createEvent(EventCreateRequestDto eventCreateRequestDto) {
        Event newEvent = Event.builder()
                .name(eventCreateRequestDto.getName())
                .description(eventCreateRequestDto.getDescription())
                .time(eventCreateRequestDto.getTime())
                .location(eventCreateRequestDto.getLocation())
                .coordinates(new Tuple<>(eventCreateRequestDto.getCoordinate_x(), eventCreateRequestDto.getCoordinate_y()))
                .build();

        //Cache

        Event createdEvent = mongoTemplate.insert(newEvent);
        System.out.println(createdEvent);

        return new EventCreateResponseDto(createdEvent.getId());
    }
}
