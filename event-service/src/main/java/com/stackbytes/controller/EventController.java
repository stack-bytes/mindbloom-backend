package com.stackbytes.controller;

import com.stackbytes.model.dto.EventCreateRequestDto;
import com.stackbytes.model.dto.EventCreateResponseDto;
import com.stackbytes.model.dto.MapEventResponseDto;
import com.stackbytes.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @CrossOrigin
    @PostMapping
    private ResponseEntity<EventCreateResponseDto> createEvent(@RequestBody EventCreateRequestDto eventCreateRequestDto) {

        EventCreateResponseDto createdEvent = eventService.createEvent(eventCreateRequestDto);

        return  createdEvent != null ? ResponseEntity.ok(createdEvent) : ResponseEntity.notFound().build();

    }

    @CrossOrigin
    @GetMapping("/map")
    private ResponseEntity<List<MapEventResponseDto>> getMapEvents(@RequestParam String groupId) {

        List<MapEventResponseDto> mapEventResponseDto =  eventService.getEventsFromGroup(groupId);
        return mapEventResponseDto != null ? ResponseEntity.ok(mapEventResponseDto) : ResponseEntity.notFound().build();

    }

    @CrossOrigin
    @PatchMapping
    private ResponseEntity<Boolean> joinEvent(@RequestParam String eventId, @RequestParam String userId){
        return eventService.addUserToEvent(eventId, userId)._1() ? ResponseEntity.ok(true) : ResponseEntity.notFound().build();
    }




}
