package com.shiyitiancheng.dao;

import com.shiyitiancheng.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealDao {
    //添加套餐
    void addSetmeal(Setmeal setmeal);

    //查询全部
    List<Setmeal> findAll();

    //分页查询全部，或者按条件查询
    List<Setmeal> findByCondition(String queryString);

    //添加套餐和检查项
    void setSetmealAndCheckGroup(Map map);

    //通过ID查询套餐
    Setmeal findById(Integer id);

    //根据ID查询套餐检查项
    List<Integer> findCheckgroupIdsBySetmealId(Integer id);

    void delCheckgroupIdsBySetmealId(Integer id);

    //更新套餐
    void updateSetmeal(Setmeal setmeal);

    //删除套餐
    void delSetmeal(Integer id);
}
