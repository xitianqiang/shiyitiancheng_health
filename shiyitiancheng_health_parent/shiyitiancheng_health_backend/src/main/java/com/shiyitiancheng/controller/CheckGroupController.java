package com.shiyitiancheng.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.shiyitiancheng.constant.MessageConstant;
import com.shiyitiancheng.entity.PageResult;
import com.shiyitiancheng.entity.QueryPageBean;
import com.shiyitiancheng.entity.Result;
import com.shiyitiancheng.pojo.CheckGroup;
import com.shiyitiancheng.service.CheckGroupService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 检查组管理
 *
 */
@RestController
@RequestMapping("/checkgroup")
public class CheckGroupController {

    @Reference
    private CheckGroupService checkGroupService;

    //新增检查组
    @RequestMapping("/add")
    public Result addCheckGroup(@RequestBody CheckGroup checkGroup,Integer[] checkitemIds){
        try{
            checkGroupService.add(checkGroup, checkitemIds);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
        }
        return new Result(true,MessageConstant.ADD_CHECKGROUP_SUCCESS);
    }

    //分页查询
    @RequestMapping("/findPage")
    public PageResult findCheckGroupAll(@RequestBody QueryPageBean queryPageBean){

        PageResult pageResult = checkGroupService.pageQuery(queryPageBean);

        return pageResult;
    }

    //通过Id查询检查项
    @RequestMapping("/findById")
    public Result findCheckGroupById(Integer id){
        try {
            CheckGroup checkGroup = checkGroupService.findById(id);
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_FAIL,checkGroup);

        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKGROUP_SUCCESS);

        }
    }
    //通过Id查询
    @RequestMapping("/findCheckItemIdsByCheckGroupId")
    public Result findCheckItemIdsByCheckGroupId(Integer id){
        try {
            List<Integer> checkItemIdsByCheckGroupId = checkGroupService.findCheckItemIdsByCheckGroupId(id);
            return new Result(true, MessageConstant.QUERY_CHECKITEM_FAIL,checkItemIdsByCheckGroupId);

        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);

        }
    }
}
