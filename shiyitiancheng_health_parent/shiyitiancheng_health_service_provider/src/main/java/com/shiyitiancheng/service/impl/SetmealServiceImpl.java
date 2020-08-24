package com.shiyitiancheng.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shiyitiancheng.dao.SetmealDao;
import com.shiyitiancheng.entity.PageResult;
import com.shiyitiancheng.entity.QueryPageBean;
import com.shiyitiancheng.pojo.Setmeal;
import com.shiyitiancheng.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealDao setmealDao;

    @Override
    @Transactional
    public void addSetmeal(Setmeal setmeal,Integer[] checkgroupIds) {
        setmealDao.addSetmeal(setmeal);
        //设置检查组和检查项的多对多关联关系，操作t_checkgroup_checkitem表
        Integer setmealId = setmeal.getId();
        setSetmealAndCheckGroup(setmealId,checkgroupIds);

    }

    @Transactional
    public void setSetmealAndCheckGroup(Integer setmealId, Integer[] checkgroupIds) {
        if (checkgroupIds!=null&&checkgroupIds.length>0){
            for (Integer checkGroupId : checkgroupIds) {
                Map<String,Integer> map = new HashMap<>();
                map.put("setmealId",setmealId);
                map.put("checkgroupId",checkGroupId);
                setmealDao.setSetmealAndCheckGroup(map);
            }
        }
    }

    @Override
    public List<Setmeal> findAll() {
        return setmealDao.findAll();
    }



    @Override
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();

        PageHelper.startPage(currentPage,pageSize);
        List<Setmeal> setmeals = setmealDao.findByCondition(queryString);
        PageInfo pageInfo = new PageInfo(setmeals);

        long total = pageInfo.getTotal();
        List data = pageInfo.getList();

        return new PageResult(total,data);
    }

    @Override
    public Setmeal findById(Integer id) {
        Setmeal setmeal = setmealDao.findById(id);
        List<Integer> checkgroupIds = setmealDao.findCheckgroupIdsBySetmealId(setmeal.getId());
        setmeal.setCheckGroupIds(checkgroupIds);
        return setmeal;
    }

    @Override
    public List<Integer> findCheckgroupIdsBySetmealId(Integer id) {
        return setmealDao.findCheckgroupIdsBySetmealId(id);
    }

    @Override
    @Transactional
    public void updateSetmeal(Setmeal setmeal, Integer[] checkgroupIds) {
        setmealDao.updateSetmeal(setmeal);
        //设置检查组和检查项的多对多关联关系，操作t_checkgroup_checkitem表
        Integer setmealId = setmeal.getId();
        setmealDao.delCheckgroupIdsBySetmealId(setmealId);
        setSetmealAndCheckGroup(setmealId,checkgroupIds);
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        setmealDao.delCheckgroupIdsBySetmealId(id);
        setmealDao.delSetmeal(id);
    }
}
