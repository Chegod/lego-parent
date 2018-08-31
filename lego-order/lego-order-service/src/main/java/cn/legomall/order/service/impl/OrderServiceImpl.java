package cn.legomall.order.service.impl;

import cn.legomall.common.jedis.JedisClient;
import cn.legomall.common.pojo.LeGoResult;
import cn.legomall.mapper.TbOrderItemMapper;
import cn.legomall.mapper.TbOrderMapper;
import cn.legomall.mapper.TbOrderShippingMapper;
import cn.legomall.order.pojo.OrderInfo;
import cn.legomall.order.service.OrderService;
import cn.legomall.pojo.TbOrderItem;
import cn.legomall.pojo.TbOrderShipping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @ClassName OrderServiceImpl
 * @Description TODO
 * @Author eooy
 * @Date 2018/6/16 22:30
 * @Version 1.0
 **/
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private TbOrderMapper orderMapper;
    @Autowired
    private TbOrderItemMapper orderItemMapper;
    @Autowired
    private TbOrderShippingMapper orderShippingMapper;
    @Autowired
    private JedisClient jedisClient;

    @Value("${ORDER_ID_GEN_KEY}")
    private String ORDER_GEN_KEY;
    @Value("${ORDER_ID_BEGIN}")
    private String ORDER_ID_BEGIN;
    @Value("${ORDER_ITEM_ID_GEN_KEY}")
    private String ORDER_ITEM_ID_GEN_KEY;

    @Override
    public LeGoResult createOrder(OrderInfo orderInfo) {
        // 1、接收表单的数据
        // 2、生成订单id
        if (!jedisClient.exists(ORDER_GEN_KEY)) {
            //设置初始值
            jedisClient.set(ORDER_GEN_KEY, ORDER_ID_BEGIN);
        }
        String orderId = jedisClient.incr(ORDER_GEN_KEY).toString();
        orderInfo.setOrderId(orderId);
        orderInfo.setPostFee("0");
        //1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭
        orderInfo.setStatus(1);
        Date date = new Date();
        orderInfo.setCreateTime(date);
        orderInfo.setUpdateTime(date);
        // 3、向订单表插入数据。
        orderMapper.insert(orderInfo);
        // 4、向订单明细表插入数据
        List<TbOrderItem> orderItems = orderInfo.getOrderItems();
        for (TbOrderItem tbOrderItem : orderItems) {
            //生成明细id
            Long orderItemId = jedisClient.incr(ORDER_ITEM_ID_GEN_KEY);
            tbOrderItem.setId(orderItemId.toString());
            tbOrderItem.setOrderId(orderId);
            //插入数据
            orderItemMapper.insert(tbOrderItem);
        }
        // 5、向订单物流表插入数据。
        TbOrderShipping orderShipping = orderInfo.getOrderShipping();
        orderShipping.setOrderId(orderId);
        orderShipping.setCreated(date);
        orderShipping.setUpdated(date);
        orderShippingMapper.insert(orderShipping);
        // 6、返回e3Result。让用户看订单号
        return LeGoResult.ok(orderId);

    }
}
