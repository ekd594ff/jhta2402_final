package com.user.IntArea.dto.image;

import com.user.IntArea.entity.ImageDto;
import lombok.Data;

import java.util.UUID;

@Data
public class ImageQuotationEditDto {

    private UUID id;
    private String url;

    public ImageQuotationEditDto(ImageDto image) {
        this.id = image.getId();
        this.url = image.getUrl();
    }
}
