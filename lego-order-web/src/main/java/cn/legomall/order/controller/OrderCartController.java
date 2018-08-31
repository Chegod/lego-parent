package cn.legomall.order.controller;

import cn.legomall.cart.service.CartService;
import cn.legomall.common.pojo.LeGoResult;
import cn.legomall.order.pojo.OrderInfo;
import cn.legomall.order.service.OrderService;
import cn.legomall.pojo.TbItem;
import cn.legomall.pojo.TbUser;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 订单controller
 *
 * @ClassName OrderCartController
 * @Description TODO
 * @Author eooy
 * @Date 2018/6/11 17:09
 * @Version 1.0
 **/
@Controller
public class OrderCartController {
    @Autowired
    private CartService cartService;
    @Autowired
    private OrderService orderService;

    //http://localhost:8074/order/order-cart.html
    @RequestMapping("order/order-cart")
    public String showOrderPage(HttpServletRequest request) {
        TbUser user = (TbUser) request.getAttribute("user");
        List<TbItem> cartList = cartService.getCartList(user.getId());
        request.setAttribute("cartList", cartList);
        return "order-cart";
    }

    @RequestMapping(value="/order/create", method=RequestMethod.POST)
    public String createOrder(OrderInfo orderInfo, HttpServletRequest request) {
        // 1、接收表单提交的数据OrderInfo。
        // 2、补全用户信息。
        TbUser user = (TbUser) request.getAttribute("user");
        orderInfo.setUserId(user.getId());
        orderInfo.setBuyerNick(user.getUsername());
        // 3、调用Service创建订单。
        LeGoResult result = orderService.createOrder(orderInfo);
        //取订单号
        String orderId = result.getData().toString();
        // a)需要Service返回订单号
        request.setAttribute("orderId", orderId);
        request.setAttribute("payment", orderInfo.getPayment());
        // b)当前日期加三天。
        DateTime dateTime = new DateTime();
        dateTime = dateTime.plusDays(3);
        request.setAttribute("date", dateTime.toString("yyyy-MM-dd"));
        // 4、返回逻辑视图展示成功页面
        return "success";
    }

}
