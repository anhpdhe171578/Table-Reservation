package com.example.demo.service.impl;

import com.example.demo.dto.DishDTO;
import com.example.demo.entity.Dish;
import com.example.demo.repository.DishRepository;
import com.example.demo.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishRepository dishRepository;

    @Override
    public List<DishDTO> getAllDishes() {
        return dishRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DishDTO getDishById(Long id) {
        return dishRepository.findById(id)
                .map(this::toDTO)
                .orElse(null);
    }

    @Override
    public DishDTO createDish(DishDTO dto) {
        Dish dish = toEntity(dto);
        dish = dishRepository.save(dish);
        return toDTO(dish);
    }

    @Override
    public DishDTO updateDish(Long id, DishDTO dto) {
        return dishRepository.findById(id).map(d -> {
            d.setName(dto.getName());
            d.setPrice(dto.getPrice());
            d.setDescription(dto.getDescription());
            d.setImage(dto.getImage());
            d.setType(dto.getType());
            d.setCategoryID(dto.getCategoryId());
            return toDTO(dishRepository.save(d));
        }).orElse(null);
    }

    @Override
    public void deleteDish(Long id) {
        dishRepository.deleteById(id);
    }

    // Convert entity -> DTO
    private DishDTO toDTO(Dish d) {
        return new DishDTO(
                d.getDishId(),
                d.getName(),
                d.getPrice(),
                d.getDescription(),
                d.getImage(),
                d.getType(),
                d.getCategoryID()
        );
    }

    // Convert DTO -> entity
    private Dish toEntity(DishDTO dto) {
        Dish d = new Dish();
        d.setName(dto.getName());
        d.setPrice(dto.getPrice());
        d.setDescription(dto.getDescription());
        d.setImage(dto.getImage());
        d.setType(dto.getType());
        d.setCategoryID(dto.getCategoryId());
        return d;
    }
}
