package com.stackbytes.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stackbytes.models.Image;
import com.stackbytes.models.RabbitMqPfp;
import com.stackbytes.models.dto.InsertImageResponseDto;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {

    private final MinioClient minioClient;

    //Add Gateway
    private final String minioUrl = "http://localhost:9000/avatars-reduced/";

    private final MongoTemplate mongoTemplate;
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    private final ReduceSizeService reduceSizeService;
    public ImageService(MinioClient minioClient, MongoTemplate mongoTemplate, RabbitTemplate rabbitTemplate, ObjectMapper objectMapper, ReduceSizeService reduceSizeService) {
        this.minioClient = minioClient;
        this.mongoTemplate = mongoTemplate;
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
        this.reduceSizeService = reduceSizeService;
    }

    public InsertImageResponseDto insertImage(MultipartFile multipartFile, String name, boolean isProfilePicture) {

        if(multipartFile == null){
            return InsertImageResponseDto.builder().success(false).message("Multipart file is null").build();
        }

        String contentType = multipartFile.getContentType();

        if(contentType == null || !contentType.startsWith("image")){
            return InsertImageResponseDto.builder().success(false).message("Invalid content type").build();
        }

        if(isProfilePicture) {
            Image image = Image.builder().name(name).build();
            Image insertedImage = mongoTemplate.insert(image);
            try {
                minioClient.putObject(
                  PutObjectArgs.builder()
                          .bucket("profile-pictures")
                          .stream(multipartFile.getInputStream(), multipartFile.getSize(), -1)
                          .contentType(contentType)
                          .object(insertedImage.getId())
                            .build()
                );

                try{
                    RabbitMqPfp rabbitMqPfp = RabbitMqPfp.builder()
                                    .id(String.format("%s%s", minioUrl, insertedImage.getId()))
                                            .name(name)
                                                    .build();

                    String sf = objectMapper.writeValueAsString(rabbitMqPfp);
                    rabbitTemplate.convertAndSend( "pfp", sf);
                    System.out.println("Message sent");
                } catch ( JsonProcessingException e) {
                    return InsertImageResponseDto.builder().success(false).message(e.getMessage()).build();
                }

            } catch (Exception e) {
                return InsertImageResponseDto.builder().success(false).message("Error : " + e.getMessage()).build();
            }
            return InsertImageResponseDto.builder().success(true).message(insertedImage.getId()).build();
        } else {
            Image image = Image.builder().name(name).build();
            Image insertedImage = mongoTemplate.insert(image);
            try {
                minioClient.putObject(
                  PutObjectArgs.builder()
                          .bucket("others")
                          .stream(multipartFile.getInputStream(), multipartFile.getSize(), -1)
                          .contentType(contentType)
                          .object(insertedImage.getId())
                            .build()
                );

                try{
                    RabbitMqPfp rabbitMqPfp = RabbitMqPfp.builder()
                            .id(String.format("%s%s", minioUrl, insertedImage.getId()))
                            .name(name)
                            .build();

                    String sf = objectMapper.writeValueAsString(rabbitMqPfp);
                    rabbitTemplate.convertAndSend( "pfp", sf);
                    System.out.println("Message sent");
                } catch ( JsonProcessingException e) {
                    return InsertImageResponseDto.builder().success(false).message(e.getMessage()).build();
                }
            } catch (Exception e) {
                return InsertImageResponseDto.builder().success(false).message("Error : " + e.getMessage()).build();
            }
            return InsertImageResponseDto.builder().success(true).message(insertedImage.getId()).build();
        }
    }

    public InsertImageResponseDto insertReducedImage(MultipartFile multipartFile, String name, boolean isProfilePicture) {
        if(multipartFile == null){
            return InsertImageResponseDto.builder().success(false).message("Multipart file is null").build();
        }

        String contentType = multipartFile.getContentType();

        if(contentType == null || !contentType.startsWith("image")){
            return InsertImageResponseDto.builder().success(false).message("Invalid content type").build();
        }

        if(isProfilePicture) {
            Image image = Image.builder().name(name).build();
            Image insertedImage = mongoTemplate.insert(image);

            try {
                MultipartFile reducedImage = reduceSizeService.reduceSize(multipartFile);
                minioClient.putObject(
                  PutObjectArgs.builder()
                          .bucket("profile-pictures-reduced")
                          .stream(reducedImage.getInputStream(), reducedImage.getSize(), -1)
                          .contentType(contentType)
                          .object(insertedImage.getId())
                            .build()
                );
            } catch (Exception e) {
                return InsertImageResponseDto.builder().success(false).message("Error : " + e.getMessage()).build();
            }
            return InsertImageResponseDto.builder().success(true).message(insertedImage.getId()).build();
        } else {
            Image image = Image.builder().name(name).build();
            Image insertedImage = mongoTemplate.insert(image);

            try {
                MultipartFile reducedImage = reduceSizeService.reduceSize(multipartFile);

                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket("others-reduced")
                                .stream(reducedImage.getInputStream(), reducedImage.getSize(), -1) // Pass the size explicitly
                                .contentType(reducedImage.getContentType())
                                .object(insertedImage.getId())
                                .build()
                );
            } catch (Exception e) {
                return InsertImageResponseDto.builder().success(false).message("Error : " + e.getMessage()).build();
            }

            return InsertImageResponseDto.builder().success(true).message(insertedImage.getId()).build();
        }
    }

    public InsertImageResponseDto insertAvatarImage(MultipartFile multipartFile, String name, boolean reduced) {

        if(multipartFile == null){
            return InsertImageResponseDto.builder().success(false).message("Multipart file is null").build();
        }

        String contentType = multipartFile.getContentType();

        if(contentType == null || !contentType.startsWith("image")){
            return InsertImageResponseDto.builder().success(false).message("Invalid content type").build();
        }

        Image image = Image.builder().name(name).build();
        Image insertedImage = mongoTemplate.insert(image);

        if(reduced) {
            try {
                MultipartFile reducedImage = reduceSizeService.reduceSize(multipartFile);
                minioClient.putObject(
                  PutObjectArgs.builder()
                          .bucket("avatars-reduced")
                          .stream(reducedImage.getInputStream(), reducedImage.getSize(), -1)
                          .contentType(contentType)
                          .object(insertedImage.getId())
                            .build()
                );

                return InsertImageResponseDto.builder().success(true).message(insertedImage.getId()).build();
            } catch (Exception e) {
                return InsertImageResponseDto.builder().success(false).message("Error : " + e.getMessage()).build();
            }
        } else {
            try {
                minioClient.putObject(
                  PutObjectArgs.builder()
                          .bucket("avatars")
                          .stream(multipartFile.getInputStream(), multipartFile.getSize(), -1)
                          .contentType(contentType)
                          .object(insertedImage.getId())
                            .build()
                );
                return InsertImageResponseDto.builder().success(true).message(insertedImage.getId()).build();
            } catch (Exception e) {
                return InsertImageResponseDto.builder().success(false).message("Error : " + e.getMessage()).build();
            }
        }
    }
}