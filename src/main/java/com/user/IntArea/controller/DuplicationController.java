package com.user.IntArea.controller;

import com.user.IntArea.service.DuplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/duplication")
public class DuplicationController {
    private final DuplicationService duplicationService;

    @PostMapping("/username")
    public ResponseEntity<?> isDuplicateUsername(@RequestBody Map<String,String> data) {
        if (duplicationService.isDuplicateUsername(data)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/email")
    public ResponseEntity<?> isDuplicateEmail(@RequestBody Map<String,String> data) {
        if (duplicationService.isDuplicateEmail(data)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok().build();
    }
}
