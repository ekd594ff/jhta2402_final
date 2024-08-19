package com.user.IntArear.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/example")
public class ExampleController {

    @PostMapping("/test")
    public String test() {
        return "test";
    }

    @PostMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/signup")
    public String signup() {
        return "signup";
    }
}
