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
                .url("intarea.s3.ap-northeast-2.amazonaws.com/" + folderPath + fileName)
                .filename(fileName)
                .originalFilename(file.getOriginalFilename())
                .build());

    }
}
