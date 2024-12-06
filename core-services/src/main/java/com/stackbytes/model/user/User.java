package com.stackbytes.model.user;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "users")
@Builder
@Getter
public class User {
    @Id
    private String userId;
    private String name;
    private Date birthday;
    private String email;
    private String pfpUrl;
    private List<String> groups;
    private List<String> events;
    private List<String> interests;
    private Date createdAt;
    private String countryCode;
}
