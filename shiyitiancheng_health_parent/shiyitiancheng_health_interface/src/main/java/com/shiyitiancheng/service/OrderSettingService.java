package com.shiyitiancheng.service;

import com.shiyitiancheng.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

public interface OrderSettingService {
    void addOrderSetting(List<OrderSetting> data);
    List<Map> getOrderSettingByMonth(String date);
    void editNumberByDate(OrderSetting orderSetting);
}
