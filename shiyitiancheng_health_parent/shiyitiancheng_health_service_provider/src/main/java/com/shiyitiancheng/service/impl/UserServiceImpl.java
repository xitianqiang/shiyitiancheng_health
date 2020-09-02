package com.shiyitiancheng.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.shiyitiancheng.dao.PermissionDao;
import com.shiyitiancheng.dao.RoleDao;
import com.shiyitiancheng.dao.UserDao;
import com.shiyitiancheng.pojo.Permission;
import com.shiyitiancheng.pojo.Role;
import com.shiyitiancheng.pojo.User;
import com.shiyitiancheng.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

@Service(interfaceClass = UserService.class)
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PermissionDao permissionDao;

    @Override
    public User findByUsername(String username) {
        User user = userDao.findByUsername(username);
        if (user == null){
            return null;
        }
        Integer userId = user.getId();
        //根据用户ID查询对应角色
        Set<Role> roles = roleDao.findByUserId(userId);
        for (Role role : roles) {
            Integer id = role.getId();
            Set<Permission> permissions = permissionDao.findByRoleId(id);
            role.setPermissions(permissions);
        }
        user.setRoles(roles);

        return user;
    }
}
