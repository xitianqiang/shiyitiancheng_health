package com.shiyitiancheng.service;

import com.shiyitiancheng.pojo.User;

public interface UserService {
    User findByUsername(String username);
}
