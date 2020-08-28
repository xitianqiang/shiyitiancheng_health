package com.shiyitiancheng.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.shiyitiancheng.constant.MessageConstant;
import com.shiyitiancheng.dao.MemberDao;
import com.shiyitiancheng.dao.OrderDao;
import com.shiyitiancheng.dao.OrderSettingDao;
import com.shiyitiancheng.entity.Result;
import com.shiyitiancheng.pojo.Member;
import com.shiyitiancheng.pojo.Order;
import com.shiyitiancheng.pojo.OrderSetting;
import com.shiyitiancheng.service.OrderService;
import com.shiyitiancheng.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderSettingDao orderSettingDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderDao orderDao;

    @Override
    @Transactional
    public Result order(Map map) throws Exception {
//        检查用户所选择的预约日期是否已经预约设置,如果没有设置则无法进行预约
        String orderDate =(String) map.get("orderDate");
        OrderSetting orderSetting = orderSettingDao.findByOrderDate(DateUtils.parseString2Date(orderDate));
        if (orderSetting == null){
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }

//        检查用户所选择的预约日期是否已经约满,如果已经约满则无法预约
        int number = orderSetting.getNumber();
        int reservations = orderSetting.getReservations();
        if (reservations>= number){
            return new Result(false,MessageConstant.ORDER_FULL);
        }

//        检查用户是否重复预约
        String telephone = (String) map.get("telephone");
        Member member = memberDao.findByTelephone(telephone);
        if (member!=null){
            Integer memberId = member.getId();
            Date order_Date = DateUtils.parseString2Date(orderDate);
            String setmealId = (String) map.get("setmealId");
            Order order = new Order();
            order.setMemberId(memberId);
            order.setOrderDate(order_Date);
            order.setSetmealId(Integer.parseInt(setmealId));

            List<Order> orders = orderDao.findByCondition(order);
            if (orders != null && orders.size()>0){
                //说明用户在重复预约,无法完成重复预约
                return new Result(false,MessageConstant.HAS_ORDERED);
            }

        }else{
            //        检查当前用户是否为会员,是直接完成预约,否自动进行注册并进行
            member = new Member();
            member.setPhoneNumber(telephone);
            member.setName((String) map.get("name"));
            member.setIdCard((String) map.get("idCard"));
            member.setSex((String) map.get("sex"));
            member.setRegTime(new Date());
            memberDao.add(member);
        }

//        预约成功,更新当日的已预约人数
        Order order = new Order();
        order.setMemberId(member.getId());
        order.setOrderDate(DateUtils.parseString2Date(orderDate));
        order.setOrderType((String) map.get("orderType"));
        order.setOrderStatus(Order.ORDERSTATUS_NO);
        order.setSetmealId(Integer.parseInt((String) map.get("setmealId")));
        orderDao.add(order);

        orderSetting.setReservations(orderSetting.getReservations()+1);//设置已预约人数



        return new Result(true,MessageConstant.ORDER_SUCCESS,order.getId());
    }

    @Override
    public Map findById(Integer id) throws Exception {
        Map map = orderDao.findById4Detail(id);
        if (map!=null){
            Date orderDate = (Date) map.get("orderDate");
            map.put("orderDate",DateUtils.parseDate2String(orderDate));
        }
        return map;
    }
}
