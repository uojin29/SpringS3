package com.example.springs3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.springs3.domain.Image;
import com.example.springs3.dto.S3Dto;
import com.example.springs3.repository.S3Repository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3Service {
    private final S3Repository s3Repository;
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadMultipartFile(MultipartFile multipartFile, String dirName) throws IOException{
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File 전환 실패"));
        return uploadFileToS3(uploadFile, dirName);
    }

    // upload file
    private String uploadFileToS3(File uploadFile, String dirName){
        UUID uuid = UUID.randomUUID();
        String fileName = dirName + "/" + uploadFile.getName() + "_" + uuid;
        String filePath = dirName + "/" + uploadFile.getName();

        putS3(uploadFile, fileName);
        save(S3Dto.from(fileName, filePath));
        removeNewFile(uploadFile);

        return uploadFile.getName();
    }

    // put object to s3
    private String putS3(File uploadFile, String fileName){
        amazonS3.putObject(new PutObjectRequest(bucket, fileName, uploadFile)
                .withCannedAcl(CannedAccessControlList.PublicRead)
        );
        return amazonS3.getUrl(bucket, fileName).toString();
    }

    // remove temp file
    private void removeNewFile(File targetFile){
        if(targetFile.delete()){
            log.info("파일이 삭제되었습니다.");
        }else{
            log.info("파일이 삭제되지 못했습니다.");
        }
    }

    // multipart to file
    private Optional<File> convert(MultipartFile file) throws  IOException {
        File convertFile = new File(file.getOriginalFilename());
        if(convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }

    // save to db
    public void save(S3Dto s3Dto) {
        Image image = Image.from(s3Dto);
        s3Repository.save(image);
    }
}
