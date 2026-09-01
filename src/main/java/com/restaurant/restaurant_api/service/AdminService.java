package com.restaurant.restaurant_api.service;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.restaurant.restaurant_api.dto.request.CreateManagerRequestDto;
import com.restaurant.restaurant_api.dto.response.RegisterResponseDto;
import com.restaurant.restaurant_api.model.Role;
import com.restaurant.restaurant_api.model.User;
import com.restaurant.restaurant_api.repository.RoleRepository;
import com.restaurant.restaurant_api.repository.UserRepository;

@Service
public class AdminService {
	
	private static final String MANAGER_ROLE ="MANAGER";
	
	
	@Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    
    public RegisterResponseDto createManager(CreateManagerRequestDto request) {

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
        user.getRoles().add(managerRole); // MANAGER directly — never touches CUSTOMER

        User saved = userRepository.save(user);

        return new RegisterResponseDto(
                saved.getId(),
                saved.getFirstName(),
                saved.getLastName(),
                saved.getEmail(),
                saved.getPhone(),
                saved.isActive(),
                saved.getRoles().stream().map(Role::getName).collect(Collectors.toSet()),
                saved.getCreatedAt()
        );
    }

}
