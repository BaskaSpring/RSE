package com.baska.RSE.Services;

import com.baska.RSE.Models.User;

public interface UserService {
    void save(User user);
    User findByUsername(String username);
}
