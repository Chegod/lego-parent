package cn.legomall.cart.service;

import cn.legomall.common.pojo.LeGoResult;
import cn.legomall.pojo.TbItem;

import java.util.List;

/**购物车service
 * @ClassName CartService
 * @Description TODO
 * @Author eooy
 * @Date 2018/5/24 22:04
 * @Version 1.0
 **/
public interface CartService {
    //向redis中添加购物车列表
    LeGoResult addCartItem(Long userId, Long itemId, Integer num);
    //合并购物车
    LeGoResult mergeCartList(List<TbItem> cartListFromCookie, Long userId);
    //从redis中获取购物车列表
    List<TbItem> getCartList(Long userId);
    //更新服务端的购物车
    LeGoResult updateCartItemNum(Long userId, Long itemId, Integer num);

    LeGoResult deleteCartItem(Long userId, Long itemId);
}
