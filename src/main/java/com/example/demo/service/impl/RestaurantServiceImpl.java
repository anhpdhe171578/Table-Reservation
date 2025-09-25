package com.example.demo.service.impl;

import com.example.demo.dto.RestaurantDTO;
import com.example.demo.entity.Restaurant;
import com.example.demo.repository.RestaurantRepository;
import com.example.demo.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Override
    public List<RestaurantDTO> getAllRestaurants() {
        return restaurantRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RestaurantDTO getRestaurantById(Long id) {
        return restaurantRepository.findById(id)
                .map(this::toDTO)
                .orElse(null);
    }

    @Override
    public RestaurantDTO createRestaurant(RestaurantDTO dto) {
        Restaurant restaurant = toEntity(dto);
        restaurant.setCreatedAt(LocalDateTime.now());
        restaurant = restaurantRepository.save(restaurant);
        return toDTO(restaurant);
    }

    @Override
    public RestaurantDTO updateRestaurant(Long id, RestaurantDTO dto) {
        return restaurantRepository.findById(id).map(r -> {
            r.setAddress(dto.getAddress());
            r.setUpdatedAt(LocalDateTime.now());
            return toDTO(restaurantRepository.save(r));
        }).orElse(null);
    }

    @Override
    public void deleteRestaurant(Long id) {
        restaurantRepository.deleteById(id);
    }

    private RestaurantDTO toDTO(Restaurant r) {
        return new RestaurantDTO(
                r.getRestaurantID(),
                r.getAddress(),
                r.getCreatedAt(),
                r.getUpdatedAt()
        );
    }

    private Restaurant toEntity(RestaurantDTO dto) {
        Restaurant r = new Restaurant();
        r.setAddress(dto.getAddress());
        return r;
    }
}
