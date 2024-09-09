package com.user.IntArea.dto.image;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Data
public class TestImageDto {

    private UUID refId;
    private String title;
    private MultipartFile image;
}
