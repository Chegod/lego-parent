package cn.legomall.order.interceptor;

import cn.legomall.cart.service.CartService;
import cn.legomall.common.jedis.JedisClient;
import cn.legomall.common.pojo.LeGoResult;
import cn.legomall.common.utils.CookieUtils;
import cn.legomall.common.utils.JsonUtils;
import cn.legomall.pojo.TbItem;
import cn.legomall.pojo.TbUser;
import cn.legomall.sso.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 订单拦截器
 *
 * @ClassName OrderCartInterceptor
 * @Description TODO
 * @Author eooy
 * @Date 2018/6/11 18:19
 * @Version 1.0
 **/
public class LoginInterceptor implements HandlerInterceptor {
    @Value("${COOKIE_TOKEN_KEY}")
    private String COOKIE_TOKEN_KEY;
    @Value("${SSO_URL}")
    private String SSO_URL;
    @Value("${LG_CART}")
    private String LG_CART;
    @Autowired
    private UserService userService;
    @Autowired
    private CartService cartService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //从cookie中取出token,也就是jsessionid
        String token = CookieUtils.getCookieValue(request, COOKIE_TOKEN_KEY, true);
        if (StringUtils.isNotBlank(token)) {
            //用户未登录,redirect=request.getRequestURL()是为了登录后返回此页面
            response.sendRedirect(SSO_URL + "/page/login?redirect=" + request.getRequestURL());
            return false;
        }
        //如果token不为空就到redis中查找是否有此用户的信息
        LeGoResult leGoResult = userService.getUserByToken(token);
        if (leGoResult.getStatus() != 200) {
            //用户登录已经过期,redirect=request.getRequestURL()是为了登录后返回此页面
            response.sendRedirect(SSO_URL + "/page/login?redirect=" + request.getRequestURL());
            return false;
        }
        //用户已经登录,从结果集中取出user
        TbUser user = (TbUser) leGoResult.getData();
        //将用户信息放到request域中
        request.setAttribute("user", user);

        //查询cookie中是否还有购物车列表
        String json = CookieUtils.getCookieValue(request, LG_CART, true);
        if (StringUtils.isNotBlank(json)) {
            cartService.mergeCartList(JsonUtils.jsonToList(json,TbItem.class),user.getId());
            CookieUtils.setCookie(request,response,LG_CART,"");
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
