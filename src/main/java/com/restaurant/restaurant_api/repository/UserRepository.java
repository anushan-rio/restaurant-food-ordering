package com.restaurant.restaurant_api.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.restaurant.restaurant_api.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
    
    List<User> findManagersByCreatedByUserId(@Param("userId") Long userId);
}