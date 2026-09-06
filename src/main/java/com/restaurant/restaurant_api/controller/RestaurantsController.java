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

import com.restaurant.restaurant_api.dto.request.RestaurantsRequestDto;
import com.restaurant.restaurant_api.dto.response.RegisterResponseDto;
import com.restaurant.restaurant_api.dto.response.RestaurantsReponseDto;
import com.restaurant.restaurant_api.payload.ApiResponseDto;
import com.restaurant.restaurant_api.service.RestaurantsService;

import jakarta.validation.Valid;
@RestController
@RequestMapping("/api/admin")
public class RestaurantsController {
	
	@Autowired
	private RestaurantsService restaurantsService;

	@PostMapping("/restaurants")
	public ResponseEntity<ApiResponseDto<RestaurantsReponseDto>> createRestaurants(
	        @Valid @RequestBody RestaurantsRequestDto request,@RequestParam Long userId) {

	    try {
	        RestaurantsReponseDto response = restaurantsService.createRestaurant(request,userId);

	        return ResponseEntity
	                .status(HttpStatus.CREATED)
	                .body(ApiResponseDto.success("Restaurant created successfully", response));

	    } catch (RuntimeException ex) {
	        return ResponseEntity
	                .status(HttpStatus.CONFLICT)
	                .body(ApiResponseDto.failure(ex.getMessage()));
	    }
	}
	
	@GetMapping("/Getrestaurants")
	public ResponseEntity<ApiResponseDto<List<RestaurantsReponseDto>>> GetrestaurantsByID(@RequestParam Long userId) {

	    try {
	    	List<RestaurantsReponseDto> response = restaurantsService.getRestaurantsByUserId(userId);

	        return ResponseEntity
	                .status(HttpStatus.CREATED)
	                .body(ApiResponseDto.success("Restaurant created successfully", response));

	    } catch (RuntimeException ex) {
	        return ResponseEntity
	                .status(HttpStatus.CONFLICT)
	                .body(ApiResponseDto.failure(ex.getMessage()));
	    }
	}
	
	

	@DeleteMapping("/restaurants/{id}")
	public ResponseEntity<ApiResponseDto<RestaurantsReponseDto>> deleteRestaurant(@PathVariable Long id) {

		try {
	        restaurantsService.deleteRestaurant(id);

	        return ResponseEntity
	                .status(HttpStatus.OK)
	                .body(ApiResponseDto.success("Restaurant deleted successfully", null));

	    } catch (RuntimeException ex) {
	        return ResponseEntity
	                .status(HttpStatus.NOT_FOUND)
	                .body(ApiResponseDto.failure(ex.getMessage()));
	    }	
	}
	
	@PutMapping("/restaurants/{id}")
	public ResponseEntity<ApiResponseDto<RestaurantsReponseDto>> updateRestaurant(@PathVariable Long id,@Valid @RequestBody RestaurantsRequestDto request) {

	    try {
	        RestaurantsReponseDto response = restaurantsService.updateRestaurant(id, request);

	        return ResponseEntity
	                .status(HttpStatus.OK)
	                .body(ApiResponseDto.success("Restaurant updated successfully", response));

	    } catch (RuntimeException ex) {
	        return ResponseEntity
	                .status(HttpStatus.NOT_FOUND)
	                .body(ApiResponseDto.failure(ex.getMessage()));
	    }
	}

}
