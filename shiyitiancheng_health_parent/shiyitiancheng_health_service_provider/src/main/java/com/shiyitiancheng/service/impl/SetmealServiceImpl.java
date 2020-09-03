package com.shiyitiancheng.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shiyitiancheng.constant.RedisConstant;
import com.shiyitiancheng.dao.SetmealDao;
import com.shiyitiancheng.entity.PageResult;
import com.shiyitiancheng.entity.QueryPageBean;
import com.shiyitiancheng.pojo.Setmeal;
import com.shiyitiancheng.service.SetmealService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import redis.clients.jedis.JedisPool;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealDao setmealDao;

    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

//    从属性文件中读取要生成的html对应的目录
//    @Value("D:/developProject/shiyitiancheng_health/shiyitiancheng_health_parent/shiyitiancheng_health_mobile/src/main/webapp/pages")
    @Value("${path}")
    private String outPutPath;

//    新增套餐，同时关联检查组
    @Override
    @Transactional
    public void addSetmeal(Setmeal setmeal,Integer[] checkgroupIds) {
        setmealDao.addSetmeal(setmeal);
        //设置检查组和检查项的多对多关联关系，操作t_checkgroup_checkitem表

        String fileName = setmeal.getImg();
//        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,fileName);
        jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES,fileName);
        Integer setmealId = setmeal.getId();//获取套餐id
        setSetmealAndCheckGroup(setmealId,checkgroupIds);

//         当添加套餐后需要重新生成静态页面（套餐列表页面，套餐详情页面）
        generateMobileStaticHtml();


    }

//    生成当前方法所需的静态页面
    public void generateMobileStaticHtml(){
//        在生成静态页面之前需要查询数据
        List<Setmeal> list = setmealDao.findAll();

//        需要生成套餐列表的静态页面
        generateMobileSetmealListHtml(list);



//        需要生成套餐详情的静态页面
        generateMobileSetmealDetailHtml(list);

    }

//    生成套餐列表静态页面
    public void generateMobileSetmealListHtml(List<Setmeal> list){
        Map map = new HashMap();
//        为模板提供数据，用于生成静态页面
        map.put("setmealList",list);
        genarateHtml("mobile_setmeal.ftl","m_setmeal.html",map);
    }

//    生成套餐详情静态页面（可能多个）
    public void generateMobileSetmealDetailHtml(List<Setmeal> list){

        for (Setmeal setmeal : list) {
            Map map = new HashMap();
//        为模板提供数据，用于生成静态页面
            map.put("setmeal",setmealDao.findById(setmeal.getId()));
            genarateHtml("mobile_setmeal_detail.ftl","setmeal_detail_"+setmeal.getId()+".html",map);
        }

    }

//    通用的方法，用于生成静态页面
    public void genarateHtml(String templateName,String htmlPageName,Map<String,Object> dataMap){
//        创建配置对象
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        Writer out = null;
        try {
            Template template = configuration.getTemplate(templateName);
//            构造输出流
            out = new FileWriter(new File(outPutPath+"/"+htmlPageName));

//            输出文件
            template.process(dataMap,out);

            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        String fileName = setmeal.getImg();
        jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES,fileName);
        Integer setmealId = setmeal.getId();
        setmealDao.delCheckgroupIdsBySetmealId(setmealId);
        setSetmealAndCheckGroup(setmealId,checkgroupIds);


//         当編輯套餐后需要重新生成静态页面（套餐列表页面，套餐详情页面）
        generateMobileStaticHtml();
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        setmealDao.delCheckgroupIdsBySetmealId(id);
        setmealDao.delSetmeal(id);
    }

    @Override
    public List<String> findSetmealNames() {

        return setmealDao.findSetmealNames();
    }

    @Override
    public List<Map<String, Object>> findSetmealCount() {

        return setmealDao.findSetmealCount();
    }
}
