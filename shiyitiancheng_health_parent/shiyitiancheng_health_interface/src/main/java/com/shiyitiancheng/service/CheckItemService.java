package com.shiyitiancheng.service;

import com.shiyitiancheng.entity.PageResult;
import com.shiyitiancheng.entity.QueryPageBean;
import com.shiyitiancheng.pojo.CheckItem;

import java.util.List;

/**
 * 服务接口
 */
public interface CheckItemService {
    void add(CheckItem checkItem);
    PageResult pageQuery(QueryPageBean queryPageBean);
    void deleteById(Integer id);
    CheckItem findById(Integer id);
    void edit(CheckItem checkItem);
    List<CheckItem> findAll();
}
