package com.shiyitiancheng.dao;

import com.github.pagehelper.Page;
import com.shiyitiancheng.pojo.CheckItem;

import java.util.List;

public interface CheckItemDao {
    void add(CheckItem checkItem);
    Page<CheckItem> selectByCondition(String queryString);
    long findCountByCheckItemId(Integer id);
    void deleteById(Integer id);
    void edit(CheckItem checkItem);
    CheckItem findById(Integer id);
    List<CheckItem> findAll();
}
