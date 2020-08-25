package com.shiyitiancheng.dao;

import com.shiyitiancheng.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderSettingDao {
    void addOrderSetting(OrderSetting orderSetting);
    void editNumberByOrderDate(OrderSetting orderSetting);
    OrderSetting findConutByOrderDate(Date orderDate);
    List<Map> getOrderSettingByMonth(Map map);

}
