package cn.legomall.controller;

import cn.legomall.common.pojo.LeGoResult;
import cn.legomall.search.service.SearchItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 将商品信息导入solr索引库中
 * @ClassName SearchItemController
 * @Description TODO
 * @Author eooy
 * @Date 2018/5/9 16:25
 * @Version 1.0
 **/
@Controller
public class SearchItemController {
    @Autowired
    private SearchItemService searchItemService;

    @RequestMapping(value = "/index/item/import",method = RequestMethod.POST)
    @ResponseBody
    public LeGoResult importAllItem(){
        return searchItemService.importAllItem();
    }
}
