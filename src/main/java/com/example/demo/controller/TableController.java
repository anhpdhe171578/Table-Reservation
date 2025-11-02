package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.TableDTO;
import com.example.demo.service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tables")
public class TableController {

    @Autowired
    private TableService tableService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public ResponseEntity<ApiResponse<List<TableDTO>>> getAllTables() {
        List<TableDTO> tables = tableService.getAllTables();
        return ResponseEntity.ok(new ApiResponse<>("success", "Lấy danh sách bàn thành công", tables));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<TableDTO>> getTableById(@PathVariable Long id) {
        TableDTO tableDTO = tableService.getTableById(id);
        if (tableDTO == null) {
            return ResponseEntity.ok(new ApiResponse<>("error", "Không tìm thấy bàn", null));
        }
        return ResponseEntity.ok(new ApiResponse<>("success", "Lấy bàn thành công", tableDTO));
    }
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<TableDTO>> createTable(@RequestBody TableDTO dto) {
        TableDTO created = tableService.createTable(dto);
        return ResponseEntity.ok(new ApiResponse<>("success", "Tạo bàn thành công", created));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<TableDTO>> updateTable(
            @PathVariable Long id,
            @RequestBody TableDTO dto) {
        TableDTO updated = tableService.updateTable(id, dto);
        if (updated == null) return ResponseEntity.ok(new ApiResponse<>("error", "Không tìm thấy bàn", null));
        return ResponseEntity.ok(new ApiResponse<>("success", "Cập nhật bàn thành công", updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteTable(@PathVariable Long id) {
        tableService.deleteTable(id);
        return ResponseEntity.ok(new ApiResponse<>("success", "Xóa bàn thành công", null));
    }
}
