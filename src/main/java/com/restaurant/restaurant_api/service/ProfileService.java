package com.restaurant.restaurant_api.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.restaurant.restaurant_api.model.User;
import com.restaurant.restaurant_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
public class ProfileService {

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private UserRepository userRepository;

    public String uploadProfilePicture(Long userId, MultipartFile file) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        try {
            Map uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", "profile_pictures",
                            "public_id", "user_" + userId,
                            "overwrite", true
                    )
            );

            String imageUrl = (String) uploadResult.get("secure_url");
            user.setProfilePictureUrl(imageUrl);
            userRepository.save(user);

            return imageUrl;
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload profile picture: " + e.getMessage());
        }
    }
    
    
    public String getProfilePicture(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getProfilePictureUrl() == null) {
            throw new RuntimeException("Profile picture not set for this user");
        }

        return user.getProfilePictureUrl();
    }
}