package com.shiyitiancheng.service;

import com.shiyitiancheng.entity.PageResult;
import com.shiyitiancheng.entity.QueryPageBean;
import com.shiyitiancheng.pojo.CheckGroup;
import com.shiyitiancheng.pojo.Setmeal;

import java.util.List;

public interface CheckGroupService {
    void add(CheckGroup checkGroup,Integer[] checkitemIds);
    PageResult pageQuery(QueryPageBean queryPageBean);
    CheckGroup findById(Integer id);
    List<CheckGroup> findAll();
    List<Integer> findCheckItemIdsByCheckGroupId(Integer id);
    void updateCheckGroup(CheckGroup checkGroup, Integer[] checkitemIds);
    void deleteById(Integer id);


}
