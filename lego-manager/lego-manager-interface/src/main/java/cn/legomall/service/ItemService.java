package cn.legomall.service;

import cn.legomall.common.pojo.EasyUIDataGridResult;
import cn.legomall.common.pojo.LeGoResult;
import cn.legomall.pojo.TbItem;
import cn.legomall.pojo.TbItemDesc;

/**
 * 商品管理service
 *
 * @ClassName ItemService
 * @Description TODO
 * @Author eooy
 * @Date 2018/4/16 22:48
 * @Version 1.0
 **/
public interface ItemService {
    //根据商品id查询商品信息，返回json数据。
    TbItem getItemById(Long id);

    //显示item-list.jsp商品列表页面,分页查询,封装EasyUIDataGridResult
    EasyUIDataGridResult getItemList(Integer page, Integer rows);

    //接收item-add.jsp页面表单提交的数据,写入数据库中
    LeGoResult addItem(TbItem tbItem, String desc);

    //在item-list.jsp中点击编辑按钮后,需要查询tb_item_desc表获得回显的数据
    LeGoResult getDescription(Long itemId);

    //在item-list.jsp中点击编辑按钮后,加载商品规格
    LeGoResult getItemParam(Long itemId);

    //编辑商品后,接收item-edit.jsp提交的数据,然后更新到数据库中
    LeGoResult updateItem(TbItem tbItem, String desc);

    //删除商品
    LeGoResult deleteItem(Long[] ids);

    //下架商品
    LeGoResult noinstockItem(Long[] ids);

    //上架商品
    LeGoResult reshelfItem(Long[] ids);
}
