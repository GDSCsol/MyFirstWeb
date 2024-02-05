package org.example.security.controller;

import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class JwtController {

    @GetMapping("/hello")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("hello");
    }

}
