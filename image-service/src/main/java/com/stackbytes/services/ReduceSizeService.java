package com.stackbytes.services;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.mock.web.MockMultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class ReduceSizeService {

    public MultipartFile reduceSize(MultipartFile multipartFile) throws IOException {
        BufferedImage originalImage = Thumbnails.of(multipartFile.getInputStream()).scale(1.0).asBufferedImage();

        // Calculate the target width and height (reduce by 5x)
        int width = originalImage.getWidth() / 5;
        int height = originalImage.getHeight() / 5;

        // Resize the image using Thumbnails
        BufferedImage resizedImage = Thumbnails.of(originalImage)
                .size(width, height)
                .asBufferedImage();

        String fileName = multipartFile.getOriginalFilename();
        String fileType = fileName.substring(fileName.lastIndexOf('.') + 1);

        System.out.println(fileType);

        if(!fileType.equals("png") && !fileType.equals("jpg") && !fileType.equals("jpeg")) {
            throw new IOException("Invalid file type");
        }
        // Convert the resized image to byte array
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(resizedImage, fileType, outputStream);
        byte[] resizedBytes = outputStream.toByteArray();

        return new MockMultipartFile(
                "file",
                multipartFile.getOriginalFilename(),
                multipartFile.getContentType(),
                resizedBytes
        );
    }
}
