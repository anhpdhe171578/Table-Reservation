package com.example.demo.service;

import com.example.demo.dto.RestaurantDTO;
import java.util.List;

public interface RestaurantService {
    List<RestaurantDTO> getAllRestaurants();
    RestaurantDTO getRestaurantById(Long id);
    RestaurantDTO createRestaurant(RestaurantDTO restaurantDTO);
    RestaurantDTO updateRestaurant(Long id, RestaurantDTO restaurantDTO);
    void deleteRestaurant(Long id);
}
