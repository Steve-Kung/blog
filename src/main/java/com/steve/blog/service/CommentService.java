package com.steve.blog.service;

import com.steve.blog.pojo.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> listCommentByBlogId(Long blogId);

    Comment saveComment(Comment comment);
}
