package com.user.IntArea.dto.member;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UpdateProfileDto {

    private String username;
    private String email;
    private MultipartFile file;
}
