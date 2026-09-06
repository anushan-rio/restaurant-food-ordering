	package com.restaurant.restaurant_api.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.restaurant.restaurant_api.dto.request.CreateManagerRequestDto;
import com.restaurant.restaurant_api.dto.request.RestaurantsRequestDto;
import com.restaurant.restaurant_api.dto.response.RegisterResponseDto;
import com.restaurant.restaurant_api.model.Restaurants;
import com.restaurant.restaurant_api.model.Role;
import com.restaurant.restaurant_api.model.User;
import com.restaurant.restaurant_api.repository.RoleRepository;
import com.restaurant.restaurant_api.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class AdminService {
	
	private static final String MANAGER_ROLE ="MANAGER";
	
	
	@Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    
    @Transactional
    public RegisterResponseDto createManager(CreateManagerRequestDto request, Long restaurantId, Long userId) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered: " + request.getEmail());
        }

        Role managerRole = roleRepository.findByName(MANAGER_ROLE)
                .orElseThrow(() -> new RuntimeException("Role not found: " + MANAGER_ROLE));

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setActive(true);
        user.setRestaurantId(restaurantId);
        user.setCreatedByUserId(userId);
        user.getRoles().add(managerRole);

        User saved = userRepository.save(user);

        return new RegisterResponseDto(
                saved.getId(),
                saved.getFirstName(),
                saved.getLastName(),
                saved.getEmail(),
                saved.getPhone(),
                saved.isActive(),
                saved.getRoles().stream().map(Role::getName).collect(Collectors.toSet()),
                saved.getRestaurantId(),
                saved.getCreatedByUserId(),
                saved.getCreatedAt()
        );
    }
    
    
    public List<RegisterResponseDto> getManagersByUserId(Long userId) {

        List<User> managers = userRepository.findManagersByCreatedByUserId(userId);

        return managers.stream()
                .map(user -> new RegisterResponseDto(
                        user.getId(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getEmail(),
                        user.getPhone(),
                        user.isActive(),
                        user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()),
                        user.getRestaurantId(),
                        user.getCreatedByUserId(),
                        user.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }
    
    @Transactional
    public RegisterResponseDto updateManager(Long id, CreateManagerRequestDto request) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Manager not found: " + id));

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));

        User updated = userRepository.save(user);

        return new RegisterResponseDto(
                updated.getId(),
                updated.getFirstName(),
                updated.getLastName(),
                updated.getEmail(),
                updated.getPhone(),
                updated.isActive(),
                updated.getRoles().stream().map(Role::getName).collect(Collectors.toSet()),
                updated.getRestaurantId(),
                updated.getCreatedByUserId(),
                updated.getCreatedAt()
        );
    }
    
    public void deleteManager(Long id) {

        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Manager not found: " + id);
        }

        userRepository.deleteById(id);
    }

}
