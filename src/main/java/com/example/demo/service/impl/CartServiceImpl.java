package com.example.demo.service.impl;

import com.example.demo.dto.CartItemDTO;
import com.example.demo.dto.DishDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.entity.CartItem;
import com.example.demo.entity.Dish;
import com.example.demo.entity.User;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.DishRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.CartService;
import com.example.demo.service.DishService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private DishService dishService;

    @Override
    public List<CartItemDTO> getCartByUser(UUID userId) {
        UserDTO userDTO = userService.getById(userId);
        if (userDTO == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        User user = new User();
        user.setId(userDTO.getId());
        return cartItemRepository.findByUser(user)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CartItemDTO addToCart(UUID userId, Long dishId, int quantity) {
        UserDTO userDTO = userService.getById(userId);
        if (userDTO == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        User user = new User();
        user.setId(userDTO.getId());

        DishDTO dishDTO = dishService.getDishById(dishId);
        if (dishDTO == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Dish not found");
        }
        Dish dish = new Dish();
        dish.setDishId(dishDTO.getId());

        CartItem item = cartItemRepository.findByUserAndDish(user, dish);
        if (item != null) {
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            item = CartItem.builder()
                    .user(user)
                    .dish(dish)
                    .quantity(quantity)
                    .build();
        }
        return toDTO(cartItemRepository.save(item));
    }

    @Override
    public CartItemDTO updateQuantity(UUID userId, Long dishId, int quantity) {
        UserDTO userDTO = userService.getById(userId);
        if (userDTO == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        User user = new User();
        user.setId(userDTO.getId());

        DishDTO dishDTO = dishService.getDishById(dishId);
        if (dishDTO == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Dish not found");
        }
        Dish dish = new Dish();
        dish.setDishId(dishDTO.getId());

        CartItem item = cartItemRepository.findByUserAndDish(user, dish);
        if (item != null) {
            item.setQuantity(quantity);
            return toDTO(cartItemRepository.save(item));
        }
        return null;
    }

    @Override
    public void removeFromCart(UUID userId, Long dishId) {
        UserDTO userDTO = userService.getById(userId);
        if (userDTO == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        User user = new User();
        user.setId(userDTO.getId());

        DishDTO dishDTO = dishService.getDishById(dishId);
        if (dishDTO == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Dish not found");
        }
        Dish dish = new Dish();
        dish.setDishId(dishDTO.getId());

        CartItem item = cartItemRepository.findByUserAndDish(user, dish);
        if (item != null) {
            cartItemRepository.delete(item);
        }
    }

    @Override
    public void clearCart(UUID userId) {
        UserDTO userDTO = userService.getById(userId);
        if (userDTO == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        User user = new User();
        user.setId(userDTO.getId());
        cartItemRepository.deleteAllByUser(user);
    }

    private CartItemDTO toDTO(CartItem item) {
        return new CartItemDTO(
                item.getId(),
                item.getUser().getId(),
                item.getDish().getDishId(),
                item.getQuantity()
        );
    }
}

