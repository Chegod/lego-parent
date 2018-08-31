package cn.legomall.order.service;

import cn.legomall.common.pojo.LeGoResult;
import cn.legomall.order.pojo.OrderInfo;

/**
 * @ClassName OrderService
 * @Description TODO
 * @Author eooy
 * @Date 2018/6/16 22:28
 * @Version 1.0
 **/
public interface OrderService {
    LeGoResult createOrder(OrderInfo orderInfo);
}
