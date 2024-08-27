package com.user.IntArea.dto.image;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class TestMultiImageDto {

    List<MultipartFile> images;
}
