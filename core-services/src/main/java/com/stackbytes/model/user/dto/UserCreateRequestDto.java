package com.stackbytes.model.user.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.sql.Date;
import java.util.List;

@Data
public class UserCreateRequestDto {
    private String name;
    private Date birthday;
    private String email;
    private List<String> interests;
}
