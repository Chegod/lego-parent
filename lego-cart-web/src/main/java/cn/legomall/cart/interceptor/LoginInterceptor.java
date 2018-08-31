package cn.legomall.cart.interceptor;

import cn.legomall.common.pojo.LeGoResult;
import cn.legomall.common.utils.CookieUtils;
import cn.legomall.pojo.TbUser;
import cn.legomall.sso.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**判断用户是否登录的拦截器
 * @ClassName LoginInterceptor
 * @Description TODO
 * @Author eooy
 * @Date 2018/5/24 17:40
 * @Version 1.0
 **/
public class LoginInterceptor implements HandlerInterceptor {
    @Value("${COOKIE_TOKEN_KEY}")
    private String COOKIE_TOKEN_KEY;
    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //查看是否有存放token的cookie
        String token = CookieUtils.getCookieValue(request, COOKIE_TOKEN_KEY);
        if(StringUtils.isBlank(token)){
            //没有登陆放行并结束此方法
            return true;
        }
        LeGoResult leGoResult = userService.getUserByToken(token);
        if(leGoResult.getStatus()!=200){
            return true;
        }
        //此时用户是登陆的
        TbUser user = (TbUser) leGoResult.getData();
        request.setAttribute("user",user);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
