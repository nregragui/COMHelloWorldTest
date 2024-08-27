package com.mjog.hello.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mjog.hello.service.HelloWorldService;

@RestController
public class HelloWorldController {

    private final HelloWorldService helloWorldService;

    public HelloWorldController(HelloWorldService helloWorldService) {
        this.helloWorldService = helloWorldService;
    }

    @PostMapping("/hello")
    public ResponseEntity<String> hello(@RequestBody String input) {
        String modifiedString = helloWorldService.hello(input);
        return ResponseEntity.ok(modifiedString);
    }
}
