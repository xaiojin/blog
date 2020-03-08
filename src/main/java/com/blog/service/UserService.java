package com.blog.service;

import com.blog.po.User;

public interface UserService {
    User checkUser(String username, String password);
    User addUser(User user);
    User getUserByName(String name);
}
