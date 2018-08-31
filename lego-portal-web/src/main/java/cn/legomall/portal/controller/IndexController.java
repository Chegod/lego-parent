package cn.legomall.portal.controller;

import cn.legomall.content.service.ContentService;
import cn.legomall.pojo.TbContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**商城首页controller
 * @ClassName IndexController
 * @Description TODO
 * @Author eooy
 * @Date 2018/5/1 16:43
 * @Version 1.0
 **/
@Controller
public class IndexController {
    @Value("${CONTENT_LUNBO_ID}")
    private Long categoryId;

    @Autowired
    private ContentService contentService;

    //显示商城首页,也可以是"index.html",html可以写也可以不写
    //动态显示商城首页
    @RequestMapping("/index")
    public String showIndex(Model model){
        List<TbContent> ad1List=contentService.getContentListByCid(categoryId);
        model.addAttribute("ad1List",ad1List);
        return "index";
    }
}
