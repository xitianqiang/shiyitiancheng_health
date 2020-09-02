package com.shiyitiancheng.dao;

import com.shiyitiancheng.pojo.Role;

import java.util.Set;

public interface RoleDao {
    Set<Role> findByUserId(Integer userId);
}
