package com.example.springs3.dto;

import com.example.springs3.controller.request.S3Request;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class S3Dto {
    private String imageName;
    private String imagePath;

    public static S3Dto from(String imageName, String imagePath) {
        return S3Dto.builder()
                .imageName(imageName)
                .imagePath(imagePath)
                .build();
    }
}
