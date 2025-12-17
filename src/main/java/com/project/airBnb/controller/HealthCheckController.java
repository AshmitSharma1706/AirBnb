package com.project.airBnb.controller;

import com.project.airBnb.advice.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthCheckController {

    @GetMapping("/")
    public ResponseEntity<ApiResponse<String>> status(){
        return ResponseEntity.ok(new ApiResponse<>("OK"));
    }
}
