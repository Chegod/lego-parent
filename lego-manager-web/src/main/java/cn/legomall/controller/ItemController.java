package cn.legomall.controller;

import cn.legomall.common.pojo.EasyUIDataGridResult;
import cn.legomall.common.pojo.LeGoResult;
import cn.legomall.pojo.TbItem;
import cn.legomall.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 商品管理controller
 * @ClassName ItemController
 * @Description TODO
 * @Author eooy
 * @Date 2018/4/16 12:33
 * @Version 1.0
 **/
@Controller
public class ItemController {
    @Autowired
    private ItemService itemService;

    //根据商品id查询商品信息，返回json数据。
    @RequestMapping("/item/{itemId}")
    @ResponseBody
    public TbItem  getItemById(@PathVariable Long itemId){
        TbItem tbItem = itemService.getItemById(itemId);
        return tbItem;
    }

    //显示item-list.jsp商品列表页面,并分页
    @RequestMapping("/item/list")
    @ResponseBody
    public EasyUIDataGridResult getItemList(Integer page, Integer rows){
            EasyUIDataGridResult result=itemService.getItemList(page,rows);
            return result;
    }

    //接收item-add.jsp页面表单提交的数据,写入数据库中
    @RequestMapping(value="/item/save", method=RequestMethod.POST)
    @ResponseBody
    public LeGoResult saveItem(TbItem tbItem, String desc){
        LeGoResult leGoResult = itemService.addItem(tbItem, desc);
        return leGoResult;
    }
    //在item-list.jsp中点击编辑按钮后,需要查询tb_item_desc表获得回显的数据
    @RequestMapping("/rest/item/query/item/desc/{itemId}")
    @ResponseBody
    public LeGoResult getDescription(@PathVariable Long itemId){
        LeGoResult leGoResult=itemService.getDescription(itemId);
        return leGoResult;
    }

    //在item-list.jsp中点击编辑按钮后,加载商品规格
    @RequestMapping("/rest/item/param/item/query/{itemId}")
    @ResponseBody
    public LeGoResult getItemParam(@PathVariable Long itemId){
        LeGoResult leGoResult=itemService.getItemParam(itemId);
        return leGoResult;
    }
    //编辑商品后,接收item-edit.jsp提交的数据,然后更新到数据库中
    @RequestMapping(value = "/rest/item/update",method = RequestMethod.POST)
    @ResponseBody
    public LeGoResult updateItem(TbItem tbItem,String desc){
        LeGoResult leGoResult=itemService.updateItem(tbItem,desc);
        return  leGoResult;
    }
    //删除商品
    @RequestMapping(value = "/rest/item/delete",method = RequestMethod.POST)
    @ResponseBody
    public LeGoResult deleteItem(Long[] ids){
        LeGoResult leGoResult=itemService.deleteItem(ids);
        return leGoResult;
    }
    //下架商品
    @RequestMapping(value = "/rest/item/noinstock",method = RequestMethod.POST)
    @ResponseBody
    public LeGoResult instockItem(Long[] ids){
        LeGoResult leGoResult=itemService.noinstockItem(ids);
        return leGoResult;
    }
    //上架商品
    @RequestMapping(value = "/rest/item/reshelf",method = RequestMethod.POST)
    @ResponseBody
    public LeGoResult reshelfItem(Long[] ids){
        LeGoResult leGoResult=itemService.reshelfItem(ids);
        return leGoResult;
    }
}
