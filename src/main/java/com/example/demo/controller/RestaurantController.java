package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.RestaurantDTO;
import com.example.demo.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @GetMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<List<RestaurantDTO>>> getAllRestaurants() {
        List<RestaurantDTO> restaurants = restaurantService.getAllRestaurants();
        return ResponseEntity.ok(new ApiResponse<>("success", "Lấy danh sách nhà hàng thành công", restaurants));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RestaurantDTO>> getRestaurantById(@PathVariable Long id) {
        RestaurantDTO restaurantDTO = restaurantService.getRestaurantById(id);
        if (restaurantDTO == null) {
            return ResponseEntity.ok(new ApiResponse<>("error", "Không tìm thấy nhà hàng", null));
        }
        return ResponseEntity.ok(new ApiResponse<>("success", "Lấy nhà hàng thành công", restaurantDTO));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<RestaurantDTO>> createRestaurant(
            @RequestBody RestaurantDTO dto) {
        RestaurantDTO created = restaurantService.createRestaurant(dto);
        return ResponseEntity.ok(new ApiResponse<>("success", "Tạo nhà hàng thành công", created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RestaurantDTO>> updateRestaurant(
            @PathVariable Long id,
            @RequestBody RestaurantDTO dto) {
        RestaurantDTO updated = restaurantService.updateRestaurant(id, dto);
        if (updated == null) return ResponseEntity.ok(new ApiResponse<>("error", "Không tìm thấy nhà hàng", null));
        return ResponseEntity.ok(new ApiResponse<>("success", "Cập nhật nhà hàng thành công", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRestaurant(@PathVariable Long id) {
        restaurantService.deleteRestaurant(id);
        return ResponseEntity.ok(new ApiResponse<>("success", "Xóa nhà hàng thành công", null));
    }
}
