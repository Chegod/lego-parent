package cn.legomall.controller;

import cn.legomall.service.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 后台页面跳转controller
 * @ClassName PageController
 * @Description TODO
 * @Author eooy
 * @Date 2018/4/23 21:34
 * @Version 1.0
 **/
@Controller
public class PageController {
    private ItemService itemService;
    //显示后台首页
    @RequestMapping("/")
    public String showBackgroundIndex(){
        return "index";
    }
    /*
    <li data-options="attributes:{'url':'item-add'}">新增商品</li>对应item-add.jsp
    <li data-options="attributes:{'url':'item-list'}">查询商品</li>对应item-list.jsp
    <li data-options="attributes:{'url':'item-param-list'}">规格参数</li>对应item-paramlist.jap
    */
    @RequestMapping("/{page}")
    public String showBackgroundPage(@PathVariable String page){
        return page;
    }
}
