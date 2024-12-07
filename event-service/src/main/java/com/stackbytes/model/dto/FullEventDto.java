package com.stackbytes.model.dto;

import com.stackbytes.model.Event;
import com.stackbytes.model.ref.EventParticipantRef;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.Date;
import java.util.List;


public class FullEventDto extends Event {


    public FullEventDto(String id, String name, String description, String groupId, Date time, String location, List<EventParticipantRef> eventParticipantRefs, String coordinate_x, String coordinate_y, Integer participants) {
        super(id, name, description, groupId, time, location, eventParticipantRefs, coordinate_x, coordinate_y, participants);
    }
}
