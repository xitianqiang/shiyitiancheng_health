package com.shiyitiancheng.dao;

import com.shiyitiancheng.pojo.Permission;

import java.util.Set;

public interface PermissionDao {
    Set<Permission> findByRoleId(Integer roleId);
}
