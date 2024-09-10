package com.user.IntArea.dto.image;

import com.user.IntArea.entity.Image;
import lombok.Data;

import java.util.UUID;

@Data
public class ImagePortfolioEditDto {

    private UUID id;
    private String url;

    public ImagePortfolioEditDto(Image image) {
        this.id = image.getId();
        this.url = image.getUrl();
    }
}
