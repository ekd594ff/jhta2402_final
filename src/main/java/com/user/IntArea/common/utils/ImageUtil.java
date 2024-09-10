package com.user.IntArea.common.utils;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.user.IntArea.dto.image.ImageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ImageUtil {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    @Value("${cloud.aws.region.static}")
    private String region;

    public Optional<ImageDto> uploadS3(MultipartFile file, UUID refId, int index) {

        String folderPath = "upload/" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd")) + "/";
        String fileName = index + "_" + UUID.randomUUID() + "." + StringUtils.getFilenameExtension(file.getOriginalFilename());

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        try {
            amazonS3Client.putObject(bucket, folderPath + fileName, file.getInputStream(), metadata);
        } catch (IOException e) {
            return Optional.empty();
        }

        return Optional.of(ImageDto.builder()
                .refId(refId)
                .url("https://" + bucket + ".s3." + region + ".amazonaws.com/" + folderPath + fileName)
                .filename(fileName)
                .originalFilename(file.getOriginalFilename())
                .build());

    }

    // 기존 S3 이미지 이름 변경
    public Optional<ImageDto> renameS3(String originalUrl, UUID refId, int index) {

        String sourceKey = originalUrl.split(".amazonaws.com/")[1];
        String extension = originalUrl.substring(originalUrl.lastIndexOf('.') + 1);

        String folderPath = "upload/" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd")) + "/";
        String fileName = index + "_" + UUID.randomUUID() + "." + extension;

        amazonS3Client.copyObject(
                bucket,
                sourceKey,
                bucket,
                folderPath + fileName
        );

        amazonS3Client.deleteObject(bucket, sourceKey);

        return Optional.of(ImageDto.builder()
                .refId(refId)
                .url("https://" + bucket + ".s3." + region + ".amazonaws.com/" + folderPath + fileName)
                .filename(fileName)
                .build());
    }

    // S3 이미지 삭제
    public void deleteS3(String url) {

        String sourceKey = url.split(".amazonaws.com/")[1];

        amazonS3Client.deleteObject(bucket, sourceKey);
    }
}
