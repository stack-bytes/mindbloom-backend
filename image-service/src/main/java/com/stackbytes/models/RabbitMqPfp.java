package com.stackbytes.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RabbitMqPfp {
    private String name;
    private String id;

}
