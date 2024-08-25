package com.user.IntArea.controller;

import com.user.IntArea.service.SolutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/solution")
@RequiredArgsConstructor
public class SolutionController {

    private final SolutionService solutionService;


}
