package com.stackbytes.models;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "images")
@Data
@Builder
public class Image {
    @Id
    private String id;

    private String name;
}
