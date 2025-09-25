package com.example.demo.service;

import com.example.demo.dto.BlogDTO;

import java.util.List;

public interface BlogService {
    List<BlogDTO> getAllBlogs();
    BlogDTO getBlogById(Long id);
    BlogDTO createBlog(BlogDTO blogDTO);
    BlogDTO updateBlog(Long id, BlogDTO blogDTO);
    void deleteBlog(Long id);
}
