package cn.legomall.sso.controller;

import cn.legomall.common.pojo.LeGoResult;
import cn.legomall.common.utils.CookieUtils;
import cn.legomall.pojo.TbUser;
import cn.legomall.sso.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * 用户信息处理controller
 *
 * @ClassName UserController
 * @Description TODO
 * @Author eooy
 * @Date 2018/5/16 16:19
 * @Version 1.0
 **/
@Controller
public class UserController {
    @Autowired
    private UserService userService;
    @Value("${COOKIE_TOKEN_KEY}")
    private String COOKIE_TOKEN_KEY;

    //校验用户名是否重复,两次输入的密码是否相同,邮箱是否重复
    @RequestMapping("/user/check/{param}/{type}")
    @ResponseBody
    public LeGoResult checkData(@PathVariable String param, @PathVariable Integer type) {
        String decodeParam=null;
        try {
            decodeParam = URLDecoder.decode(param, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        LeGoResult leGoResult = userService.checkData(decodeParam, type);
        return leGoResult;
    }

    //用户注册成功,接收注册信息写入数据库中
    @RequestMapping(value = "/user/register", method = RequestMethod.POST)
    @ResponseBody
    public LeGoResult userRegister(TbUser user) {
        LeGoResult leGoResult = userService.userRegister(user);
        return leGoResult;
    }

    //用户登录
    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    @ResponseBody
    public LeGoResult userLogin(String username, String password,
                                HttpServletRequest request, HttpServletResponse response) {
        //result的Object data中包含token(相当于jsessionid)
        LeGoResult leGoResult = userService.userLogin(username, password);
        //将token存入cookie中,放到客户端
        String token = leGoResult.getData().toString();
        //cookie不需要设置过期时间
        CookieUtils.setCookie(request,response,COOKIE_TOKEN_KEY,token);
        //需要客户端登录成功,其中包含了token
        return leGoResult;
    }

}
