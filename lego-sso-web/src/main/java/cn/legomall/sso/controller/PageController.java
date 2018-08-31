package cn.legomall.sso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 显示注册和登录页面
 *
 * @ClassName PageController
 * @Description TODO
 * @Author eooy
 * @Date 2018/5/16 15:55
 * @Version 1.0
 **/
@Controller
public class PageController {

    //显示注册页面
    @RequestMapping("/page/register")
    public String showRegister() {
        return "register";
    }
    //显示登录页面
    //response.sendRedirect(SSO_URL + "/page/login?redirect=" + request.getRequestURL());
    @RequestMapping("/page/login")
    public String showLogin(String redirect, Model model){
        model.addAttribute("redirect",redirect);
        return "login";
    }
}
