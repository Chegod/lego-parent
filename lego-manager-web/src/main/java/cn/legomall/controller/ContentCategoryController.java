package cn.legomall.controller;

import cn.legomall.common.pojo.EasyUITreeNode;
import cn.legomall.common.pojo.LeGoResult;
import cn.legomall.content.service.ContentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 商城内容controller
 * @ClassName ContentCategoryController
 * @Description TODO
 * @Author eooy
 * @Date 2018/5/2 16:54
 * @Version 1.0
 **/
@Controller
public class ContentCategoryController {
    @Autowired
    private ContentCategoryService contentCategoryService;

    //查询tb_content_category表,返回初始化easyUITree需要的节点列表
    @RequestMapping(value = "/content/category/list",method = RequestMethod.GET)
    @ResponseBody
    public List<EasyUITreeNode> getContentCatList(@RequestParam(value = "id",defaultValue = "0") Long parentId){
        List<EasyUITreeNode> treeNodeList=contentCategoryService.getContentCatList(parentId);
        return treeNodeList;
    }

    //添加商城内容分类的节点
    @RequestMapping(value = "/content/category/create",method = RequestMethod.POST)
    @ResponseBody
    public LeGoResult addContentCategory(Long parentId,String name){
       LeGoResult leGoResult= contentCategoryService.addContentCategory(parentId,name);
       return leGoResult;
    }
    //重命名(更新)商城内容分类的节点
    @RequestMapping(value = "/content/category/update",method = RequestMethod.POST)
    @ResponseBody
    public LeGoResult updateContentCategory(Long id,String name){
        LeGoResult leGoResult=contentCategoryService.updateContentCategory(id,name);
        return leGoResult;
    }
    //删除商城内容分类的节点
    @RequestMapping(value = "/content/category/delete",method = RequestMethod.POST)
    @ResponseBody
    public LeGoResult deleteContentCategory(Long id){
        LeGoResult leGoResult=contentCategoryService.deleteContentCategory(id);
        return leGoResult;
    }
}
