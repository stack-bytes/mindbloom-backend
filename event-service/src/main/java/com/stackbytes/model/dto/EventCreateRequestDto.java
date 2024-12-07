package com.stackbytes.model.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.Date;

@Getter
@Data
public class EventCreateRequestDto {
    private String name;
    private String description;
    private Date time;
    private String groupId;
    private String location;
    private String coordinate_x;
    private String coordinate_y;
}
