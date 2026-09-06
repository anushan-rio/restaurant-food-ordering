package com.restaurant.restaurant_api.controller;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.restaurant.restaurant_api.config.JwtUtil;
import com.restaurant.restaurant_api.dto.request.LoginRequestDto;
import com.restaurant.restaurant_api.dto.request.RegisterRequestDto;
import com.restaurant.restaurant_api.dto.response.LoginResponseDto;
import com.restaurant.restaurant_api.dto.response.RegisterResponseDto;
import com.restaurant.restaurant_api.payload.ApiResponseDto;
import com.restaurant.restaurant_api.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;
    
    

    @PostMapping("/register")
    public ResponseEntity<ApiResponseDto<RegisterResponseDto>> register(
            @Valid @RequestBody RegisterRequestDto request) {

        try {
        	
            RegisterResponseDto response = authService.register(request);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ApiResponseDto.success("User registered successfully", response));

        } catch (RuntimeException ex) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(ApiResponseDto.failure(ex.getMessage()));
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<ApiResponseDto<LoginResponseDto>> login(
            @Valid @RequestBody LoginRequestDto request) {

        try {
            LoginResponseDto response = authService.login(request);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(ApiResponseDto.success("Login successful", response));

        } catch (RuntimeException ex) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponseDto.failure(ex.getMessage()));
        }
    }
    
}