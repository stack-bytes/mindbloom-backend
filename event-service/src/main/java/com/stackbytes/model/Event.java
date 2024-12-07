package com.stackbytes.model;


import com.stackbytes.model.ref.EventParticipantRef;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "events")
@Data
@Builder
@AllArgsConstructor
public class Event {
    @Id
    private String id;
    private String name;
    private String description;
    private String groupId;
    private Date time;
    private String location;
    private List<EventParticipantRef> eventParticipantRefs;
    private String coordinate_x;
    private String coordinate_y;
    private Integer participants;
}
