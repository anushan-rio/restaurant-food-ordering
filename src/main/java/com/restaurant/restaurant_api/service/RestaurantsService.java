package com.restaurant.restaurant_api.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.restaurant.restaurant_api.controller.RestaurantsController;
import com.restaurant.restaurant_api.dto.request.RestaurantsRequestDto;
import com.restaurant.restaurant_api.dto.response.RestaurantsReponseDto;
import com.restaurant.restaurant_api.model.Restaurants;
import com.restaurant.restaurant_api.repository.RestaurantsRepository;

import jakarta.transaction.Transactional;
@Service
public class RestaurantsService {
	
	
	@Autowired
    private RestaurantsRepository restaurantRepository;
 
	@Transactional
    public RestaurantsReponseDto createRestaurant(RestaurantsRequestDto request,Long userId) {
 
		Restaurants restaurant = new Restaurants();
        restaurant.setName(request.getName());
        restaurant.setDescription(request.getDescription());
        restaurant.setPhone(request.getPhone());
        restaurant.setEmail(request.getEmail());
        restaurant.setAddress(request.getAddress());
        restaurant.setActive(true);
        restaurant.setUserId(userId);
 
        Restaurants saved = restaurantRepository.save(restaurant);
 
        return new RestaurantsReponseDto(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getDescription(),
                restaurant.getPhone(),
                restaurant.getEmail(),
                restaurant.getAddress(),
                restaurant.isActive(),
                saved.getUserId(),
                restaurant.getCreatedAt(),
                restaurant.getUpdatedAt()
        );
    }
		
	
	public List<RestaurantsReponseDto> getRestaurantsByUserId(Long userId) {

	    List<Restaurants> restaurants = restaurantRepository.findByUserId(userId);

	    return restaurants.stream()
	            .map(restaurant -> new RestaurantsReponseDto(
	                    restaurant.getId(),
	                    restaurant.getName(),
	                    restaurant.getDescription(),
	                    restaurant.getPhone(),
	                    restaurant.getEmail(),
	                    restaurant.getAddress(),
	                    restaurant.isActive(),
	                    restaurant.getUserId(),
	                    restaurant.getCreatedAt(),
	                    restaurant.getUpdatedAt()
	            ))
	            .collect(Collectors.toList());
	}
	
	
	public void deleteRestaurant(Long id) {

	    if (!restaurantRepository.existsById(id)) {
	        throw new RuntimeException("Restaurant not found: " + id);
	    }

	    restaurantRepository.deleteById(id);
	}
	
	@Transactional
	public RestaurantsReponseDto updateRestaurant(Long id, RestaurantsRequestDto request) {

	    Restaurants restaurant = restaurantRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("Restaurant not found: " + id));

	    restaurant.setName(request.getName());
	    restaurant.setDescription(request.getDescription());
	    restaurant.setPhone(request.getPhone());
	    restaurant.setEmail(request.getEmail());
	    restaurant.setAddress(request.getAddress());

	    Restaurants updated = restaurantRepository.save(restaurant);

	    return new RestaurantsReponseDto(
	            updated.getId(),
	            updated.getName(),
	            updated.getDescription(),
	            updated.getPhone(),
	            updated.getEmail(),
	            updated.getAddress(),
	            updated.isActive(),
	            updated.getUserId(),
	            updated.getCreatedAt(),
	            updated.getUpdatedAt()
	    );
	}
}
