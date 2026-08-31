package com.restaurant.restaurant_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.restaurant.restaurant_api.model.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);
}