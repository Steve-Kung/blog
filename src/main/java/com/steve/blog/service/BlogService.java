package com.steve.blog.service;

import com.steve.blog.pojo.Blog;
import com.steve.blog.vo.BlogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BlogService {
    Blog getBlog(Long id);
    Blog getAndConvert(Long id);
    Blog saveBlog(Blog blog);
    void deleteBlog(Long id);
    Blog updateBlog(Long id, Blog blog);
    Page<Blog> listBlogs(Pageable pageable, BlogQuery blog);
    Page<Blog> listBlogs(Pageable pageable);
    Page<Blog> listBlogs(Long tagId, Pageable pageable);
    Page<Blog> listBlogs(String query, Pageable pageable);
    List<Blog> listRecommendBlogTop(Integer size);
}
