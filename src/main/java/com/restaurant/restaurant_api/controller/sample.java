package com.restaurant.restaurant_api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class sample {
	@GetMapping("/hello")
    public String hello() {
        return "Restaurant API is running!";
    }
}
	