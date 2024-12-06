package com.stackbytes.model.dto;

import com.stackbytes.model.ref.EventParticipantRef;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.yaml.snakeyaml.util.Tuple;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class MapEventResponseDto {
    private String id;
    private String name;
    private String description;
    private Date time;
    private String location;
    private String coordinate_x;
    private String coordinate_y;
    private Integer participants;
}
