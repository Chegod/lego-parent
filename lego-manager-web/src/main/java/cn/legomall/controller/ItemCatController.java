package cn.legomall.controller;

import cn.legomall.common.pojo.EasyUITreeNode;
import cn.legomall.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 商品类目结构controller
 *
 * @ClassName ItemCatController
 * @Description TODO
 * @Author eooy
 * @Date 2018/4/27 16:37
 * @Version 1.0
 **/
@Controller
public class ItemCatController {
    @Autowired
    private ItemCatService itemCatService;

    @RequestMapping("/item/cat/list")
    @ResponseBody
    public List<EasyUITreeNode> getItemCatList(@RequestParam(value="id",defaultValue = "0")Long parentId) {
        List<EasyUITreeNode> treeNodeList = itemCatService.getItemCatList(parentId);
        return treeNodeList;
    }

}
