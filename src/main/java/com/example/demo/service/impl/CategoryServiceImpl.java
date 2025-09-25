package com.example.demo.service.impl;

import com.example.demo.dto.CategoryDTO;
import com.example.demo.entity.Category;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDTO getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .map(this::toDTO)
                .orElse(null);
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO dto) {
        Category category = toEntity(dto);
        category.setCreatedAt(LocalDateTime.now());
        category = categoryRepository.save(category);
        return toDTO(category);
    }

    @Override
    public CategoryDTO updateCategory(Long id, CategoryDTO dto) {
        return categoryRepository.findById(id).map(c -> {
            c.setName(dto.getName());
            c.setUpdatedAt(LocalDateTime.now());
            return toDTO(categoryRepository.save(c));
        }).orElse(null);
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    private CategoryDTO toDTO(Category c) {
        return new CategoryDTO(
                c.getIdCategory(),
                c.getName(),
                c.getCreatedAt(),
                c.getUpdatedAt()
        );
    }

    private Category toEntity(CategoryDTO dto) {
        Category c = new Category();
        c.setName(dto.getName());
        return c;
    }
}
