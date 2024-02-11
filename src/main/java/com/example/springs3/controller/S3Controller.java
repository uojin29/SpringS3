package com.example.springs3.controller;

import com.example.springs3.controller.request.S3Request;
import com.example.springs3.dto.S3Dto;
import com.example.springs3.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/s3")
@RequiredArgsConstructor
@CrossOrigin("*")
public class S3Controller {
    private final S3Service s3Service;

    @PostMapping("/upload")
    public String upload(@RequestParam MultipartFile image,
                         @RequestParam String imageName,
                         @RequestParam String imagePath) throws IOException {
        S3Dto s3Dto = S3Dto.from(imageName, imagePath);
        s3Service.save(s3Dto);
        return s3Service.upload(image, "test");
    }

}
