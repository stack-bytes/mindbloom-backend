package com.stackbytes.model.group;

import lombok.Data;

import java.util.List;

@Data
public class Metadata {
    private List<String> tags;
    private List<String> interests;
}
