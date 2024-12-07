package com.stackbytes.model.user;

import com.fasterxml.jackson.annotation.JsonView;
import com.stackbytes.views.Views;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "users")
@Builder
@Getter
@Data
public class User {
    @Id
    @JsonView(Views.Public.class)
    private String userId;
    @JsonView(Views.Public.class)
    private String name;
    @JsonView(Views.Public.class)
    private Date birthday;

    @JsonView(Views.Private.class)
    private String email;

    @JsonView(Views.Public.class)
    private String pfpUrl;
    @JsonView(Views.Public.class)
    private List<String> groups;
    @JsonView(Views.Public.class)
    private List<String> events;
    @JsonView(Views.Public.class)
    private List<String> interests;
    @JsonView(Views.Public.class)
    private Date createdAt;
    @JsonView(Views.Public.class)
    private String countryCode;
}
