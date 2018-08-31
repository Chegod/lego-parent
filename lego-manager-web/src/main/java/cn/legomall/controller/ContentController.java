package cn.legomall.controller;

import cn.legomall.common.pojo.EasyUIDataGridResult;
import cn.legomall.common.pojo.LeGoResult;
import cn.legomall.content.service.ContentService;
import cn.legomall.pojo.TbContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 商城内容controller
 * @ClassName ContentController
 * @Description TODO
 * @Author eooy
 * @Date 2018/5/3 17:14
 * @Version 1.0
 **/
@Controller
public class ContentController {
    @Autowired
    private ContentService contentService;

    //根据内容分类id查询内容列表,要进行分页处理,page:页面当前页,rows:每一页显示多少条
    @RequestMapping(value = "/content/query/list",method = RequestMethod.GET)
    @ResponseBody
    public EasyUIDataGridResult getContentList(Long categoryId, Integer page, Integer rows){
        return contentService.getContentList(categoryId,page,rows);
    }

    //点击content.jsp中的新增按钮,会弹出一个窗口显示content-add.jsp
    @RequestMapping(value = "/content/save",method = RequestMethod.POST)
    @ResponseBody
    public LeGoResult addContent(TbContent content){
       return contentService.addContent(content);
    }
}
