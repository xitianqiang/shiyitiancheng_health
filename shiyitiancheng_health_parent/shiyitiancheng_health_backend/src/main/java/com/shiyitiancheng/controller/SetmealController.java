package com.shiyitiancheng.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.shiyitiancheng.constant.MessageConstant;
import com.shiyitiancheng.constant.RedisConstant;
import com.shiyitiancheng.entity.PageResult;
import com.shiyitiancheng.entity.QueryPageBean;
import com.shiyitiancheng.entity.Result;
import com.shiyitiancheng.pojo.Setmeal;
import com.shiyitiancheng.service.CheckGroupService;
import com.shiyitiancheng.service.SetmealService;
import com.shiyitiancheng.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.UUID;

/**
 * 体检套餐管理
 *
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Reference
    private CheckGroupService checkGroupService;

    @Reference
    private SetmealService setmealService;

    //使用JediPool操作Redis服务
    @Autowired
    private JedisPool jedisPool;

    //新增检查组
    @RequestMapping("/add")
    public Result addSetmeal(@RequestBody Setmeal setmeal, Integer[] checkgroupIds){
        try{
            setmealService.addSetmeal(setmeal,checkgroupIds);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_SETMEAL_FAIL);
        }
        return new Result(true,MessageConstant.ADD_SETMEAL_SUCCESS);
    }

    //分页查询
    @RequestMapping("/findPage")
    public PageResult findSetmealByPage(@RequestBody QueryPageBean queryPageBean){

        PageResult pageResult = setmealService.pageQuery(queryPageBean);

        return pageResult;
    }



    //通过Id查询检查组
    @RequestMapping("/findById")
    public Result findCheckGroupIdBySetmealId(Integer id){
        try {
            Setmeal setmeal = setmealService.findById(id);
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);

        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);

        }
    }



    //新增检查组
    @RequestMapping("/update")
    public Result updateCheckGroup(@RequestBody Setmeal setmeal, Integer[] checkgroupIds){
        try{
            setmealService.updateSetmeal(setmeal, checkgroupIds);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
        return new Result(true,MessageConstant.EDIT_CHECKGROUP_SUCCESS);
    }

    //删除检查项
    @RequestMapping("/delete")
    public Result delete(Integer id){

        try{
            setmealService.deleteById(id);
        }catch(Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_CHECKGROUP_FAIL);
        }
        return new Result(true, MessageConstant.DELETE_CHECKGROUP_SUCCESS);

    }

    //文件上传
    @RequestMapping("/upload")
    public Result upload(@RequestParam("imgFile") MultipartFile imgFile){

        String originalFilename = imgFile.getOriginalFilename();//原始文件名

        int i = originalFilename.lastIndexOf(".");

        String extention = originalFilename.substring(i - 1);//截取后缀名

        String fileName = UUID.randomUUID().toString()+extention;//新文件名

        try {
            //将文件上传到七牛网
            QiniuUtils.upload2Qiniu(imgFile.getBytes(),fileName);

            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,fileName);

            return new Result(true,MessageConstant.PIC_UPLOAD_SUCCESS,fileName);

        } catch (IOException e) {
            e.printStackTrace();
            return new Result(true,MessageConstant.PIC_UPLOAD_FAIL);
        }

    }
}
