package com.shiyitiancheng.dao;

import com.shiyitiancheng.pojo.CheckGroup;

import java.util.List;
import java.util.Map;

public interface CheckGroupDao {
    void add(CheckGroup checkGroup);

    void setCheckGroupAndCheckItem(Map map);

    //按条件查询
    List<CheckGroup> findByCondition(String queryString);
    //通过Id查询
    CheckGroup findById(Integer id);

    List<CheckGroup> findAll();

    List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    void updateCheckGroup(CheckGroup checkGroup);

    void delCheckGroupAndCheckItem(Integer checkGroupId);

    void delCheckGroup(Integer id);
}
