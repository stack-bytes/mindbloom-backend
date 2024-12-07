package com.stackbytes.model.group.dto;

import com.stackbytes.model.group.Metadata;
import lombok.Data;

import java.util.List;

@Data
public class CreateGroupRequestDto {
    String name;
    String description;
    private String location;
    private String coordinate_x;
    private String coordinate_y;
    private String owner;
    private List<String> members;
    private Metadata metadata;
}
