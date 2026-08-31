package com.restaurant.restaurant_api.config;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.restaurant.restaurant_api.repository.UserRepository;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                     HttpServletResponse response,
                                     FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                String email = jwtUtil.extractEmail(token);

                if (email != null && jwtUtil.isTokenValid(token, email)) {
                    userRepository.findByEmail(email).ifPresent(user -> {
                        var authToken = new UsernamePasswordAuthenticationToken(
                                user.getEmail(), null, Collections.emptyList());
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    });
                }
            } catch (Exception ex) {
                // invalid/expired token — leave SecurityContext empty, request stays unauthenticated
            }
        }

        filterChain.doFilter(request, response);
    }
}
