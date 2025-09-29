package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.BlogDTO;
import com.example.demo.dto.DishDTO;
import com.example.demo.service.BlogService;
import com.example.demo.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/blogs")
public class BlogController {
    @Autowired
    private BlogService blogService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<BlogDTO>>> getAllDishes() {
        List<BlogDTO> dishes = blogService.getAllBlogs();
        return ResponseEntity.ok(new ApiResponse<>("success", "Lấy danh sách món ăn thành công", dishes));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BlogDTO>> getDishById(@PathVariable Long id) {
        BlogDTO dishDTO = blogService.getBlogById(id);
        if (dishDTO == null) {
            return ResponseEntity.ok(new ApiResponse<>("error", "Không tìm thấy món ăn", null));
        }
        return ResponseEntity.ok(new ApiResponse<>("success", "Lấy món ăn thành công", dishDTO));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BlogDTO>> createDish(@RequestBody BlogDTO dto) {
        BlogDTO created = blogService.createBlog(dto);
        return ResponseEntity.ok(new ApiResponse<>("success", "Tạo món ăn thành công", created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BlogDTO>> updateDish(
            @PathVariable Long id,
            @RequestBody BlogDTO dto) {
        BlogDTO updated = blogService.updateBlog(id, dto);
        if (updated == null) return ResponseEntity.ok(new ApiResponse<>("error", "Không tìm thấy món ăn", null));
        return ResponseEntity.ok(new ApiResponse<>("success", "Cập nhật món ăn thành công", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDish(@PathVariable Long id) {
        blogService.deleteBlog(id);
        return ResponseEntity.ok(new ApiResponse<>("success", "Xóa món ăn thành công", null));
    }
}
