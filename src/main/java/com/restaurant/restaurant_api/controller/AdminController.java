package com.restaurant.restaurant_api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.restaurant.restaurant_api.dto.request.CreateManagerRequestDto;
import com.restaurant.restaurant_api.dto.request.RestaurantsRequestDto;
import com.restaurant.restaurant_api.dto.response.RegisterResponseDto;
import com.restaurant.restaurant_api.dto.response.RestaurantsReponseDto;
import com.restaurant.restaurant_api.payload.ApiResponseDto;
import com.restaurant.restaurant_api.service.AdminService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
	
	@Autowired
	private AdminService adminService;
	
	@PostMapping("/users/managers")
	public ResponseEntity<ApiResponseDto<RegisterResponseDto>> createManager(
	        @Valid @RequestBody CreateManagerRequestDto request,
	        @RequestParam Long restaurantId,
	        @RequestParam Long userId) {

	    try {
	        RegisterResponseDto response = adminService.createManager(request, restaurantId, userId);

	        return ResponseEntity
	                .status(HttpStatus.CREATED)
	                .body(ApiResponseDto.success("Manager account created successfully", response));

	    } catch (RuntimeException ex) {
	        return ResponseEntity
	                .status(HttpStatus.CONFLICT)
	                .body(ApiResponseDto.failure(ex.getMessage()));
	    }
	}
	
	
	@GetMapping("/users/managers/{userId}")
	public ResponseEntity<ApiResponseDto<List<RegisterResponseDto>>> getManagersByUserId(
	        @PathVariable Long userId) {

	    try {
	        List<RegisterResponseDto> response = adminService.getManagersByUserId(userId);

	        return ResponseEntity
	                .status(HttpStatus.OK)
	                .body(ApiResponseDto.success("Managers retrieved successfully", response));

	    } catch (RuntimeException ex) {
	        return ResponseEntity
	                .status(HttpStatus.NOT_FOUND)
	                .body(ApiResponseDto.failure(ex.getMessage()));
	    }
	}
	
	@PutMapping("/users/managers/{id}")
	public ResponseEntity<ApiResponseDto<RegisterResponseDto>> updateManager(
	        @PathVariable Long id,
	        @Valid @RequestBody CreateManagerRequestDto request) {

	    try {
	        RegisterResponseDto response = adminService.updateManager(id, request);

	        return ResponseEntity
	                .status(HttpStatus.OK)
	                .body(ApiResponseDto.success("Manager account updated successfully", response));

	    } catch (RuntimeException ex) {
	        return ResponseEntity
	                .status(HttpStatus.NOT_FOUND)
	                .body(ApiResponseDto.failure(ex.getMessage()));
	    }
	}
	
	@DeleteMapping("/users/managers/{id}")
	public ResponseEntity<ApiResponseDto<Void>> deleteManager(@PathVariable Long id) {

	    try {
	        adminService.deleteManager(id);

	        return ResponseEntity
	                .status(HttpStatus.OK)
	                .body(ApiResponseDto.success("Manager account deleted successfully", null));

	    } catch (RuntimeException ex) {
	        return ResponseEntity
	                .status(HttpStatus.NOT_FOUND)
	                .body(ApiResponseDto.failure(ex.getMessage()));
	    }
	}
	

}
