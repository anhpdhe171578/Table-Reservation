package com.example.demo.service.impl;

import com.example.demo.dto.AreaDTO;
import com.example.demo.dto.RestaurantDTO;
import com.example.demo.entity.Area;
import com.example.demo.entity.Restaurant;
import com.example.demo.repository.AreaRepository;
import com.example.demo.repository.RestaurantRepository;
import com.example.demo.service.AreaService;
import com.example.demo.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AreaServiceImpl implements AreaService {

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private RestaurantService restaurantService;

    @Override
    public List<AreaDTO> getAllAreas() {
        return areaRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AreaDTO getAreaById(Long id) {
        return areaRepository.findById(id)
                .map(this::toDTO)
                .orElse(null);
    }

    @Override
    public AreaDTO createArea(AreaDTO dto) {
        Area area = toEntity(dto);
        area.setCreatedAt(LocalDateTime.now());
        area = areaRepository.save(area);
        return toDTO(area);
    }

    @Override
    public AreaDTO updateArea(Long id, AreaDTO dto) {
        return areaRepository.findById(id).map(a -> {
            a.setAreaName(dto.getAreaName());
            if (dto.getRestaurantID() != null) {
                RestaurantDTO restaurantDTO = restaurantService.getRestaurantById(dto.getRestaurantID());
                if (restaurantDTO == null) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant not found");
                }
                Restaurant restaurant = new Restaurant();
                restaurant.setRestaurantID(restaurantDTO.getId());
                a.setRestaurant(restaurant);
            }
            a.setUpdatedAt(LocalDateTime.now());
            return toDTO(areaRepository.save(a));
        }).orElse(null);
    }

    @Override
    public void deleteArea(Long id) {
        areaRepository.deleteById(id);
    }

    private AreaDTO toDTO(Area a) {
        return new AreaDTO(
                a.getAreaID(),
                a.getAreaName(),
                a.getRestaurant() != null ? a.getRestaurant().getRestaurantID() : null,
                a.getCreatedAt(),
                a.getUpdatedAt()
        );
    }

    private Area toEntity(AreaDTO dto) {
        Area a = new Area();
        a.setAreaName(dto.getAreaName());
        if (dto.getRestaurantID() != null) {
            RestaurantDTO restaurantDTO = restaurantService.getRestaurantById(dto.getRestaurantID());
            if (restaurantDTO == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant not found");
            }
            Restaurant restaurant = new Restaurant();
            restaurant.setRestaurantID(restaurantDTO.getId());
            a.setRestaurant(restaurant);
        }
        return a;
    }
}