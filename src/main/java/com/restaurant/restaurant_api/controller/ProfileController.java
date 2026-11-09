package com.restaurant.restaurant_api.controller;

import com.restaurant.restaurant_api.payload.ApiResponseDto;
import com.restaurant.restaurant_api.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;
@RestController
@RequestMapping("/api/users")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @PostMapping(value = "/{userId}/profile-picture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponseDto<String>> uploadProfilePicture(
            @PathVariable Long userId,
            @RequestParam("file") MultipartFile file) {
        try {
            String imageUrl = profileService.uploadProfilePicture(userId, file);
            return ResponseEntity.ok(ApiResponseDto.success("Profile picture uploaded successfully", imageUrl));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponseDto.failure(e.getMessage()));
        }
    }
    
    @GetMapping("/{userId}/profile-picture")
    public ResponseEntity<ApiResponseDto<String>> getProfilePicture(@PathVariable Long userId) {
        try {
            String imageUrl = profileService.getProfilePicture(userId);
            return ResponseEntity.ok(ApiResponseDto.success("Profile picture fetched successfully", imageUrl));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseDto.failure(e.getMessage()));
        }
    }
    
    @PutMapping(value = "/{userId}/profile-picture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponseDto<String>> updateProfilePicture(
            @PathVariable Long userId,
            @RequestParam("file") MultipartFile file) {
        try {
            String imageUrl = profileService.uploadProfilePicture(userId, file);
            return ResponseEntity.ok(ApiResponseDto.success("Profile picture updated successfully", imageUrl));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponseDto.failure(e.getMessage()));
        }
    }
    
    
}