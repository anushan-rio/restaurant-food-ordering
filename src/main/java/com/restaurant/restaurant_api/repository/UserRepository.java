package com.restaurant.restaurant_api.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import com.restaurant.restaurant_api.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}