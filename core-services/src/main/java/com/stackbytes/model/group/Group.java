package com.stackbytes.model.group;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;
import java.util.List;

@Document(collection = "groups")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
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
    private List<String> members;
    private Metadata metadata;
    private Date created_at;
    private Date updated_at;

}
