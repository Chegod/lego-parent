package cn.legomall.cart.controller;

import cn.legomall.cart.service.CartService;
import cn.legomall.common.pojo.LeGoResult;
import cn.legomall.common.utils.CookieUtils;
import cn.legomall.common.utils.JsonUtils;
import cn.legomall.pojo.TbItem;
import cn.legomall.pojo.TbUser;
import cn.legomall.service.ItemService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 购物车Controller
 *
 * @ClassName CartController
 * @Description TODO
 * @Author eooy
 * @Date 2018/5/20 21:26
 * @Version 1.0
 **/
@Controller
public class CartController {
    @Autowired
    private ItemService itemService;
    @Value("${LG_CART}")
    private String LG_CART;
    @Value("${CART_EXPIRE}")
    private Integer CART_EXPIRE;
    @Autowired
    private CartService cartService;

    //点击添加购物车执行此方法
    @RequestMapping("/cart/add/{itemId}")
    public String addCartItem(@PathVariable Long itemId, Integer num,
                              HttpServletRequest request, HttpServletResponse response) {
        //添加商品时,要先判断用户是否登录
        Object object =  request.getAttribute("user");
        if(object!=null){
            //说明用户已经登录,将购物车列表存入redis中
            TbUser user= (TbUser) object;
            Long userId = user.getId();
           LeGoResult result= cartService.addCartItem(userId,itemId,num);
           return "cartSuccess";
        }

        //此时用户没有登录,将购物车列表存入cookie中
        List<TbItem> cartList = this.getCartList(request);
        //判断新添加的商品,在cookie的购物车列表中有没有
        boolean hasItem = false;
        for (TbItem item : cartList) {
            if (item.getId() == itemId.longValue()) {
                //发现购物车列表中已经有此商品
                item.setNum(item.getNum() + num);
                hasItem = true;
                break;
            }
        }
        //这件商品是新添加的商品
        if (!hasItem) {
            TbItem item = itemService.getItemById(itemId);
            //取出一张图片
            String image = item.getImage();
            if (StringUtils.isNotBlank(image)) {
                String[] images = image.split(",");
                item.setImage(images[0]);
            }
            item.setNum(num);
            cartList.add(item);
        }
        String json = JsonUtils.objectToJson(cartList);
        CookieUtils.setCookie(request, response, LG_CART, json, CART_EXPIRE, true);
        return "cartSuccess";
    }

    //添加完购物车之后,显示购物车列表/cart/cart.html
    @RequestMapping("/cart/cart")
    public String showCartList(HttpServletRequest request, HttpServletResponse response) {
        //从cookie中去找json形式的购物车列表
        List<TbItem> cartListFromCookie = this.getCartList(request);

        //检查用户是否登录
        Object object = request.getAttribute("user");
        if(object!=null){
            //如果登录了,就从redis中取购物车列表
            TbUser user= (TbUser) object;
            System.out.println("用户:"+user.getUsername()+",登录成功");
            /*因为添加商品时,用户登录就添加到redis中,没登录就添加到cookie,
            如果用户一开始没有登录就添加了一些商品,之后又登录添加了一些商品,这时,
            一部分商品信息在redis中一部分在cookie中,于是需要合并商品信息
            */
            if (!cartListFromCookie.isEmpty()){
                //合并购物车
                cartService.mergeCartList(cartListFromCookie,user.getId());
                //重置cookie中的数据
                CookieUtils.setCookie(request,response,LG_CART,"");
            }
           List<TbItem> cartListFromRedis= cartService.getCartList(user.getId());
            request.setAttribute("cartList", cartListFromRedis);
            return "cart";
        }

        System.out.println("用户没有登录");
        request.setAttribute("cartList", cartListFromCookie);
        return "cart";
    }

    //在购物车列表页面中,修改商品的数量,/cart/update/num/"+_thisInput.attr("itemId")+"/"+_thisInput.val() + ".action
    @RequestMapping("/cart/update/num/{itemId}/{num}")
    public LeGoResult updateNum(@PathVariable Long itemId, @PathVariable Integer num,
                                HttpServletRequest request, HttpServletResponse response) {
        //判断是否为登录状态
        Object object = request.getAttribute("user");
        if (object != null) {
            TbUser user = (TbUser) object;
            //更新服务端的购物车
            cartService.updateCartItemNum(user.getId(), itemId, num);
            return LeGoResult.ok();
        }

        //从cookie中取出商品列表
        List<TbItem> cartList = this.getCartList(request);
        //遍历列表,找出要修改的商品
        for(TbItem tbItem:cartList){
            if(tbItem.getId()==itemId.longValue()){
                tbItem.setNum(num);
            }
        }
        //将cookie发送回去
        String json = JsonUtils.objectToJson(cartList);
        CookieUtils.setCookie(request,response,LG_CART, json, CART_EXPIRE, true);
        return LeGoResult.ok();
    }
    //在购物车列表页面中,删除商品/cart/delete/${cart.id}.html
    @RequestMapping("/cart/delete/{itemId}")
    public String deleteCartItem(@PathVariable Long itemId,HttpServletRequest request,HttpServletResponse response){
        //判断用户登录状态
        Object object = request.getAttribute("user");
        if (object != null) {
            TbUser user = (TbUser) object;
            //删除服务端的购物车商品
            cartService.deleteCartItem(user.getId(), itemId);
            return "redirect:/cart/cart.html";
        }

        List<TbItem> cartList = this.getCartList(request);
        for (TbItem tbItem:cartList){
            if(tbItem.getId()==itemId.longValue()){
                cartList.remove(tbItem);
                break;
            }
        }
        String json = JsonUtils.objectToJson(cartList);
        CookieUtils.setCookie(request,response,LG_CART,json,CART_EXPIRE,true);
        //重定向
        return "redirect:/cart/cart.html";
    }

    //从cookie中取出购物车列表
    public List<TbItem> getCartList(HttpServletRequest request) {
        //取得字符串形式的购物车列表
        String cookieValue = CookieUtils.getCookieValue(request, LG_CART, true);
        if (StringUtils.isNotBlank(cookieValue)) {
            List<TbItem> itemList = JsonUtils.jsonToList(cookieValue, TbItem.class);
            return itemList;
        }
        return new ArrayList<>();
    }
}
