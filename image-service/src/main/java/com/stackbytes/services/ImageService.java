package com.stackbytes.services;

import com.stackbytes.models.Image;
import com.stackbytes.models.dto.InsertImageResponseDto;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {

    private final MinioClient minioClient;

    private final MongoTemplate mongoTemplate;

    private final ReduceSizeService reduceSizeService;
    public ImageService(MinioClient minioClient, MongoTemplate mongoTemplate, ReduceSizeService reduceSizeService) {
        this.minioClient = minioClient;
        this.mongoTemplate = mongoTemplate;
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
}