package com.stackbytes.model.ref;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventParticipantRef {
    private String id;
    private String name;
    private String pfpUrl;
}
