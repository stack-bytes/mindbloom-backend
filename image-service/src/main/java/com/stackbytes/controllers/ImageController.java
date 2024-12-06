package com.stackbytes.controllers;

import com.stackbytes.models.dto.InsertImageResponseDto;
import com.stackbytes.services.ImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/insert")
    public ResponseEntity<InsertImageResponseDto> insertImage(@RequestParam("file")MultipartFile file, @RequestParam("name") String name, @RequestParam("isProfilePicture") boolean isProfilePicture){
        InsertImageResponseDto response = imageService.insertImage(file, name, isProfilePicture);
        if(response.isSuccess()){
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(response);
    }

    @PostMapping("/insert-reduced")
    public ResponseEntity<InsertImageResponseDto> insertReducedImage(@RequestParam("file")MultipartFile file, @RequestParam("name") String name, @RequestParam("isProfilePicture") boolean isProfilePicture) {
        InsertImageResponseDto response = imageService.insertReducedImage(file, name, isProfilePicture);
        if(response.isSuccess()){
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(response);
    }
}