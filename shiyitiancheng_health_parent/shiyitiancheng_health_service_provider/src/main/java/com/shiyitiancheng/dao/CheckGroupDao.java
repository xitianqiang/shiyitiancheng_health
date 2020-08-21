package com.shiyitiancheng.dao;

import com.shiyitiancheng.pojo.CheckGroup;
import com.shiyitiancheng.pojo.CheckItem;

import java.util.List;
import java.util.Map;

public interface CheckGroupDao {
    void add(CheckGroup checkGroup);

    void setCheckGroupAndCheckItem(Map map);

    void edit(CheckItem checkItem);

    //按条件查询
    List<CheckGroup> findByCondition(String queryString);
    //通过Id查询
    CheckGroup findById(Integer id);

    List<CheckGroup> findAll();

    List<Integer> findCheckItemIdsByCheckGroupId(Integer id);
}
