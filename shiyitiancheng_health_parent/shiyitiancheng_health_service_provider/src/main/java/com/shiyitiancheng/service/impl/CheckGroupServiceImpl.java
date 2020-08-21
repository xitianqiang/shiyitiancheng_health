package com.shiyitiancheng.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shiyitiancheng.dao.CheckGroupDao;
import com.shiyitiancheng.entity.PageResult;
import com.shiyitiancheng.entity.QueryPageBean;
import com.shiyitiancheng.pojo.CheckGroup;
import com.shiyitiancheng.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 检查组服务
 */
@Service(interfaceClass = CheckGroupService.class)
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    private CheckGroupDao checkGroupDao;

    //新增检查组，同时需要让检查组关联检查项
    @Override
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
        //新增检查组，操作t_checkgroup表
        checkGroupDao.add(checkGroup);
        //设置检查组和检查项的多对多关联关系，操作t_checkgroup_checkitem表
        Integer checkGroupId = checkGroup.getId();
        if (checkitemIds!=null&checkitemIds.length>0){
            for (Integer checkitemId : checkitemIds) {
                Map<String,Integer> map = new HashMap<>();
                map.put("checkgroupId",checkGroupId);
                map.put("checkitemId",checkitemId);
                checkGroupDao.setCheckGroupAndCheckItem(map);
            }
        }

    }

    @Override
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        if (currentPage!=1&&queryString!=null){
            currentPage=1;
        }
        PageHelper.startPage(currentPage,pageSize);
        List<CheckGroup> list = checkGroupDao.findByCondition(queryString);
        PageInfo pageInfo = new PageInfo(list);
        long total = pageInfo.getTotal();
        List<CheckGroup> data = pageInfo.getList();

        return new PageResult(total,data);
    }

    @Override
    public CheckGroup findById(Integer id) {
        return checkGroupDao.findById(id);
    }

    @Override
    public void edit(CheckGroup checkGroup) {

    }


    @Override
    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }

    @Override
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id) {

        return checkGroupDao.findCheckItemIdsByCheckGroupId(id);
    }


}
