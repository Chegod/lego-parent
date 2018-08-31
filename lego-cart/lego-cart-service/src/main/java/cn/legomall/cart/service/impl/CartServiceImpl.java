package cn.legomall.cart.service.impl;

import cn.legomall.cart.service.CartService;
import cn.legomall.common.jedis.JedisClient;
import cn.legomall.common.pojo.LeGoResult;
import cn.legomall.common.utils.JsonUtils;
import cn.legomall.mapper.TbItemMapper;
import cn.legomall.pojo.TbItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**购物车cartserviceImpl
 * @ClassName CartServiceImpl
 * @Description TODO
 * @Author eooy
 * @Date 2018/5/24 22:06
 * @Version 1.0
 **/
@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private JedisClient jedisClient;
    @Value("${CART_REDIS_PRE}")
    private String CART_REDIS_PRE;
    @Autowired
    private TbItemMapper itemMapper;

    //向redis中添加购物车列表
    @Override
    public LeGoResult addCartItem(Long userId, Long itemId, Integer num) {
        //先查询redis中有没有添加的这件商品
        Boolean exists = jedisClient.hexists(CART_REDIS_PRE +":"+ userId, itemId + "");
        if (exists){
            String json = jedisClient.hget(CART_REDIS_PRE +":"+ userId, itemId + "");
            TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
            //这是添加商品,不是修改商品数量
            tbItem.setNum(tbItem.getNum()+num);
            String toJson = JsonUtils.objectToJson(tbItem);
            jedisClient.hset(CART_REDIS_PRE +":"+ userId, itemId + "",toJson);
            return LeGoResult.ok();
        }
        //如果redis中没有这件商品
        TbItem tbItem = itemMapper.selectByPrimaryKey(itemId);
        tbItem.setNum(num);
        //可能有多张图片
        String image = tbItem.getImage();
        if (StringUtils.isNotBlank(image)){
            String[] split = image.split(",");
            tbItem.setImage(split[0]);
        }
        String toJson = JsonUtils.objectToJson(tbItem);
        jedisClient.hset(CART_REDIS_PRE +":"+ userId, itemId + "",toJson);
        return LeGoResult.ok();
    }
    //合并购物车
    @Override
    public LeGoResult mergeCartList(List<TbItem> cartListFromCookie, Long userId) {
        for(TbItem item:cartListFromCookie){
            addCartItem(userId,item.getId(),item.getNum());
            }
       return LeGoResult.ok();
    }
    //从redis中获取购物车列表
    @Override
    public List<TbItem> getCartList(Long userId) {
        List<String> strList = jedisClient.hvals(CART_REDIS_PRE +":"+ userId);
        List<TbItem> cartList = new ArrayList<>();
        for (String str:strList){
            TbItem item = JsonUtils.jsonToPojo(str, TbItem.class);
            cartList.add(item);
        }
        return cartList;
    }

    //更新redis的购物车
    @Override
    public LeGoResult updateCartItemNum(Long userId, Long itemId, Integer num) {
        //从redis中取出要更新的商品
        String json = jedisClient.hget(CART_REDIS_PRE + ":" + userId, itemId + "");
        TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
        item.setNum(num);
        String toJson = JsonUtils.objectToJson(item);
        jedisClient.hset(CART_REDIS_PRE + ":" + userId, itemId + "",toJson);
        return LeGoResult.ok();
    }
    //删除商品列表
    @Override
    public LeGoResult deleteCartItem(Long userId, Long itemId) {
        jedisClient.hdel(CART_REDIS_PRE + ":" + userId, itemId + "");
        return LeGoResult.ok();
    }
}
