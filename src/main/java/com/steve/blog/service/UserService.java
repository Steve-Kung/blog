package com.steve.blog.service;

import com.steve.blog.pojo.User;

public interface UserService {

    public User checkUser(String username, String password);
}
