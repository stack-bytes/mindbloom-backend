package com.stackbytes.model.group;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "groups")
@AllArgsConstructor
@NoArgsConstructor
public class Group {
    @Id
    private String id;
    private String avatarUrl;
    private String name;
    private String description;
    private String location;
    private String coordinate_x;
    private String coordinate_y;
    private String owner;

}
