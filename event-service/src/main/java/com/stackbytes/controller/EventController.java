package com.stackbytes.controller;

import com.stackbytes.model.dto.EventCreateRequestDto;
import com.stackbytes.model.dto.EventCreateResponseDto;
import com.stackbytes.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    private ResponseEntity<EventCreateResponseDto> createEvent(@RequestBody EventCreateRequestDto eventCreateRequestDto) {

        EventCreateResponseDto createdEvent = eventService.createEvent(eventCreateRequestDto);

        return  createdEvent != null ? ResponseEntity.ok(createdEvent) : ResponseEntity.notFound().build();

    }

}
