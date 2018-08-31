package cn.legomall.item.controller;

import cn.legomall.common.pojo.LeGoResult;
import cn.legomall.item.pojo.Item;
import cn.legomall.pojo.TbItem;
import cn.legomall.pojo.TbItemDesc;
import cn.legomall.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 点击搜索页面的商品,给用户显示商品的详细信息
 * @ClassName ItemController
 * @Description TODO
 * @Author eooy
 * @Date 2018/5/14 23:00
 * @Version 1.0
 **/
@Controller
public class ItemController {
    @Autowired
    private ItemService itemService;

    @RequestMapping("/item/{itemId}")
    public String showItemInfo(@PathVariable Long itemId, Model model){
        //根据id查询tb_item表
        TbItem tbItem = itemService.getItemById(itemId);
        Item item = new Item(tbItem);
        //根据id查询tb_item_desc表
        LeGoResult result = itemService.getDescription(itemId);
        TbItemDesc itemDesc = (TbItemDesc) result.getData();
        model.addAttribute("item",item);
        model.addAttribute("itemDesc",itemDesc);
        return "item";
    }
}
