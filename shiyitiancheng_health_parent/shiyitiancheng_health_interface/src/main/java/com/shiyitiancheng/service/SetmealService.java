package com.shiyitiancheng.service;

import com.shiyitiancheng.entity.PageResult;
import com.shiyitiancheng.entity.QueryPageBean;
import com.shiyitiancheng.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealService {
    void addSetmeal(Setmeal setmeal,Integer[] checkgroupIds);

    List<Setmeal> findAll();

    PageResult pageQuery(QueryPageBean queryPageBean);

    Setmeal findById(Integer id);

    List<Integer> findCheckgroupIdsBySetmealId(Integer id);

    void updateSetmeal(Setmeal setmeal, Integer[] checkgroupIds);

    void deleteById(Integer id);

    List<String> findSetmealNames();

    List<Map<String, Object>> findSetmealCount();
}
