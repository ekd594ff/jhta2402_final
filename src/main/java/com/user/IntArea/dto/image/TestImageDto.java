package com.user.IntArea.dto.image;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class TestImageDto {

    private String title;
    private MultipartFile image;
}
