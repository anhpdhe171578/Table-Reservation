package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.DishDTO;
import com.example.demo.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dishes")
public class DishController {

    @Autowired
    private DishService dishService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public ResponseEntity<ApiResponse<List<DishDTO>>> getAllDishes() {
        List<DishDTO> dishes = dishService.getAllDishes();
        return ResponseEntity.ok(new ApiResponse<>("success", "Lấy danh sách món ăn thành công", dishes));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<DishDTO>> getDishById(@PathVariable Long id) {
        DishDTO dishDTO = dishService.getDishById(id);
        if (dishDTO == null) {
            return ResponseEntity.ok(new ApiResponse<>("error", "Không tìm thấy món ăn", null));
        }
        return ResponseEntity.ok(new ApiResponse<>("success", "Lấy món ăn thành công", dishDTO));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<DishDTO>> createDish(@RequestBody DishDTO dto) {
        DishDTO created = dishService.createDish(dto);
        return ResponseEntity.ok(new ApiResponse<>("success", "Tạo món ăn thành công", created));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<DishDTO>> updateDish(
            @PathVariable Long id,
            @RequestBody DishDTO dto) {
        DishDTO updated = dishService.updateDish(id, dto);
        if (updated == null) return ResponseEntity.ok(new ApiResponse<>("error", "Không tìm thấy món ăn", null));
        return ResponseEntity.ok(new ApiResponse<>("success", "Cập nhật món ăn thành công", updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteDish(@PathVariable Long id) {
        dishService.deleteDish(id);
        return ResponseEntity.ok(new ApiResponse<>("success", "Xóa món ăn thành công", null));
    }
}
