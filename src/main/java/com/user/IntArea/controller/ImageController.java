package com.user.IntArea.controller;

import com.user.IntArea.common.utils.ImageUtil;
import com.user.IntArea.dto.image.TestImageDto;
import com.user.IntArea.dto.image.TestMultiImageDto;
import com.user.IntArea.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/image")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;
    private final ImageUtil imageUtil;


    @PostMapping("/test")
    public ResponseEntity<?> test(TestImageDto testImageDto) {
        imageUtil.uploadS3(testImageDto.getImage(), testImageDto.getRefId(), 0);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/multitest")
    public ResponseEntity<?> multiTest(TestMultiImageDto testMultiImageDto) {

        List<MultipartFile> images = testMultiImageDto.getImages();

        for (int i = 0; i < images.size(); i++) {
            imageUtil.uploadS3(images.get(i), testMultiImageDto.getRefId(), i);
        }

        return ResponseEntity.ok().build();
    }
}
