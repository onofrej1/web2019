package com.furca.service;

import com.furca.model.User;

public interface UserService {
    void save(User user);

    User findByUsername(String username);
}