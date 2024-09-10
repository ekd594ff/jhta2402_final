package com.user.IntArea.dto.image;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
public class ImageDto {

    private UUID refId;
    private String url;
    private String filename;
    private String originalFilename;

    @Builder
    public ImageDto(UUID refId, String url, String filename, String originalFilename) {
        this.refId = refId;
        this.url = url;
        this.filename = filename;
        this.originalFilename = originalFilename;
    }

    public com.user.IntArea.entity.ImageDto toImage() {
        return com.user.IntArea.entity.ImageDto.builder()
                .refId(refId)
                .url(url)
                .filename(filename)
                .originalFilename(originalFilename)
                .build();
    }
}
