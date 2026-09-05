package com.restaurant.restaurant_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.restaurant.restaurant_api.model.Restaurants;

public interface RestaurantsRepository extends JpaRepository<Restaurants, Long> {
	
	List<Restaurants> findByUserId(Long userId);	
}