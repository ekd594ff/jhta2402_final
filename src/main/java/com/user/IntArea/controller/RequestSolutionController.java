package com.user.IntArea.controller;

import com.user.IntArea.service.RequestSolutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/requestsolution")
@RequiredArgsConstructor
public class RequestSolutionController {

    private final RequestSolutionService requestSolutionService;

}
