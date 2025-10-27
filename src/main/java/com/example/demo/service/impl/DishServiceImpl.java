package com.example.demo.service.impl;

import com.example.demo.dto.DishDTO;
import com.example.demo.entity.Dish;
import com.example.demo.entity.Category;
import com.example.demo.repository.DishRepository;
import com.example.demo.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dish not found"));
    }

    @Override
    public DishDTO createDish(DishDTO dto) {
        Dish dish = toEntity(dto);
        return toDTO(dishRepository.save(dish));
    }

    @Override
    public DishDTO updateDish(Long id, DishDTO dto) {
        return dishRepository.findById(id).map(d -> {
            d.setName(dto.getName());
            d.setPrice(dto.getPrice());
            d.setDescription(dto.getDescription());
            d.setImage(dto.getImage());
            d.setType(dto.getType());

            // Nếu DTO có categoryId, chỉ set một Category với id đó
            if (dto.getCategoryId() != null) {
                Category category = new Category();
                category.setCategoryID(dto.getCategoryId());
                d.setCategory(category);
            } else {
                d.setCategory(null);
            }

            return toDTO(dishRepository.save(d));
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dish not found"));
    }

    @Override
    public void deleteDish(Long id) {
        if (!dishRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Dish not found");
        }
        dishRepository.deleteById(id);
    }

    private DishDTO toDTO(Dish d) {
        return new DishDTO(
                d.getDishId(),
                d.getName(),
                d.getPrice(),
                d.getDescription(),
                d.getImage(),
                d.getType(),
                d.getCategory() != null ? d.getCategory().getCategoryID() : null
        );
    }

    private Dish toEntity(DishDTO dto) {
        Dish d = new Dish();
        d.setName(dto.getName());
        d.setPrice(dto.getPrice());
        d.setDescription(dto.getDescription());
        d.setImage(dto.getImage());
        d.setType(dto.getType());

        if (dto.getCategoryId() != null) {
            Category category = new Category();
            category.setCategoryID(dto.getCategoryId());
            d.setCategory(category);
        }

        return d;
    }
}
