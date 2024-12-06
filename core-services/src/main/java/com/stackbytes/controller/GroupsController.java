package com.stackbytes.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/groups")
public class GroupsController {

    @GetMapping("/")
    private String sanity(){
        return "Sanity";
    }
}
