package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.AreaDTO;
import com.example.demo.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/areas")
public class AreaController {

    @Autowired
    private AreaService areaService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<AreaDTO>>> getAllAreas() {
        List<AreaDTO> areas = areaService.getAllAreas();
        return ResponseEntity.ok(new ApiResponse<>("success", "Lấy danh sách khu vực thành công", areas));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AreaDTO>> getAreaById(@PathVariable Long id) {
        AreaDTO areaDTO = areaService.getAreaById(id);
        if (areaDTO == null) {
            return ResponseEntity.ok(new ApiResponse<>("error", "Không tìm thấy khu vực", null));
        }
        return ResponseEntity.ok(new ApiResponse<>("success", "Lấy khu vực thành công", areaDTO));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AreaDTO>> createArea(@RequestBody AreaDTO dto) {
        AreaDTO created = areaService.createArea(dto);
        return ResponseEntity.ok(new ApiResponse<>("success", "Tạo khu vực thành công", created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AreaDTO>> updateArea(@PathVariable Long id, @RequestBody AreaDTO dto) {
        AreaDTO updated = areaService.updateArea(id, dto);
        if (updated == null) return ResponseEntity.ok(new ApiResponse<>("error", "Không tìm thấy khu vực", null));
        return ResponseEntity.ok(new ApiResponse<>("success", "Cập nhật khu vực thành công", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteArea(@PathVariable Long id) {
        areaService.deleteArea(id);
        return ResponseEntity.ok(new ApiResponse<>("success", "Xóa khu vực thành công", null));
    }
}
