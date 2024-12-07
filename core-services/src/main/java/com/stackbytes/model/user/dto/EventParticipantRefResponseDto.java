package com.stackbytes.model.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventParticipantRefResponseDto {
    private String id;
    private String name;
    private String pfpUrl;
}
