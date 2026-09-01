package com.restaurant.restaurant_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.restaurant.restaurant_api.dto.request.CreateManagerRequestDto;
import com.restaurant.restaurant_api.dto.response.RegisterResponseDto;
import com.restaurant.restaurant_api.payload.ApiResponseDto;
import com.restaurant.restaurant_api.service.AdminService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
	
	@Autowired
	private AdminService adminService;
	
	@PostMapping("/users/managers")
	public ResponseEntity<ApiResponseDto<RegisterResponseDto>> createManager(@Valid @RequestBody CreateManagerRequestDto request) 
	{

        try {
            RegisterResponseDto response = adminService.createManager(request);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ApiResponseDto.success("Manager account created successfully", response));

        } catch (RuntimeException ex) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(ApiResponseDto.failure(ex.getMessage()));
        }
    }

}
