package com.shiyitiancheng.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shiyitiancheng.dao.CheckItemDao;
import com.shiyitiancheng.entity.PageResult;
import com.shiyitiancheng.entity.QueryPageBean;
import com.shiyitiancheng.pojo.CheckItem;
import com.shiyitiancheng.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 检查项服务
 */
@Service(interfaceClass = CheckItemService.class)
@Transactional
public class CheckItemServiceImpl implements CheckItemService {

    @Autowired
    private CheckItemDao checkItemDao;

    @Override
    @Transactional
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    @Override
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();//查询条件
        if (currentPage!=1&&queryString!=null){
            currentPage=1;
        }
        PageHelper.startPage(currentPage, pageSize);
        List<CheckItem> page = checkItemDao.selectByCondition(queryString);
        PageInfo<CheckItem> checkItemPageInfo = new PageInfo<>(page);

        long total = checkItemPageInfo.getTotal();
        List<CheckItem> rows = checkItemPageInfo.getList();
        return new PageResult(total,rows);
    }

    //根据ID删除
    @Override
    @Transactional
    public void deleteById(Integer id) {
        //判断当前检查项是否已经关联到检查组
//        long count = checkItemDao.findCountByCheckItemId(id);
//        if (count>0){
//            // 当前检查项已经被关联到检查级，不允许删除
//            new RuntimeException();
//        }
        checkItemDao.deleteById(id);
    }

    @Override
    public CheckItem findById(Integer id) {
        return checkItemDao.findById(id);
    }

    @Override
    @Transactional
    public void edit(CheckItem checkItem) {
        checkItemDao.edit(checkItem);
    }

    @Override
    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }


}
