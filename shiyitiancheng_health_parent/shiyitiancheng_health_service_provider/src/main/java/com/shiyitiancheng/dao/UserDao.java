package com.shiyitiancheng.dao;

import com.shiyitiancheng.pojo.User;

public interface UserDao {
    User findByUsername(String username);
}
