package com.restaurant.restaurant_api.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.restaurant.restaurant_api.config.JwtUtil;
import com.restaurant.restaurant_api.dto.request.LoginRequestDto;
import com.restaurant.restaurant_api.dto.request.RegisterRequestDto;
import com.restaurant.restaurant_api.dto.response.LoginResponseDto;
import com.restaurant.restaurant_api.dto.response.RegisterResponseDto;
import com.restaurant.restaurant_api.model.Role;
import com.restaurant.restaurant_api.model.User;
import com.restaurant.restaurant_api.repository.RoleRepository;
import com.restaurant.restaurant_api.repository.UserRepository;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private static final String DEFAULT_ROLE = "CUSTOMER";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtil jwtUtil;


    @Transactional
    public RegisterResponseDto register(RegisterRequestDto request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered: " + request.getEmail());
        }

        Role customerRole = roleRepository.findByName(DEFAULT_ROLE)
                .orElseThrow(() -> new RuntimeException("Role not found: " + DEFAULT_ROLE));

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setActive(true);
        user.getRoles().add(customerRole); // always CUSTOMER at registration — no exceptions

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
    
    public LoginResponseDto login(LoginRequestDto request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!user.isActive()) {
            throw new RuntimeException("Account is deactivated. Contact support.");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtUtil.generateToken(user.getEmail());

        Set<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        return new LoginResponseDto(token, user.getId(), user.getEmail(), roleNames);
    }
    
}


	