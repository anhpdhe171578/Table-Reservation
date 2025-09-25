package com.example.demo.service.impl;

import com.example.demo.dto.BlogDTO;
import com.example.demo.entity.Blog;
import com.example.demo.repository.BlogRepository;
import com.example.demo.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository blogRepository;

    @Override
    public List<BlogDTO> getAllBlogs() {
        return blogRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BlogDTO getBlogById(Long id) {
        return blogRepository.findById(id)
                .map(this::toDTO)
                .orElse(null);
    }

    @Override
    public BlogDTO createBlog(BlogDTO dto) {
        Blog blog = toEntity(dto);
        blog = blogRepository.save(blog);
        return toDTO(blog);
    }

    @Override
    public BlogDTO updateBlog(Long id, BlogDTO dto) {
        return blogRepository.findById(id).map(b -> {
            b.setTitle(dto.getTitle());
            b.setSubTitle(dto.getSubTitle());
            b.setDescription(dto.getDescription());
            b.setImage(dto.getImage());
            b.setCreateAtBy(dto.getCreateAtBy());
            b.setStatus(dto.getStatus());
            return toDTO(blogRepository.save(b));
        }).orElse(null);
    }

    @Override
    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }

    private BlogDTO toDTO(Blog b) {
        return new BlogDTO(
                b.getIdBlog(),
                b.getImage(),
                b.getTitle(),
                b.getSubTitle(),
                b.getDescription(),
                b.getCreateAtBy(),
                b.getStatus(),
                b.getCreatedAt(),
                b.getUpdatedAt()
        );
    }

    private Blog toEntity(BlogDTO dto) {
        Blog b = new Blog();
        b.setImage(dto.getImage());
        b.setTitle(dto.getTitle());
        b.setSubTitle(dto.getSubTitle());
        b.setDescription(dto.getDescription());
        b.setCreateAtBy(dto.getCreateAtBy());
        b.setStatus(dto.getStatus());
        return b;
    }
}
