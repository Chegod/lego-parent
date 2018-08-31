package cn.legomall.search.controller;

import cn.legomall.common.pojo.SearchResult;
import cn.legomall.search.service.SearchItemService;
import cn.legomall.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;

/**
 * 点击首页搜索按钮,将搜索框中关键字提交到此处,
 * 然后到solr索引库中查询数据,返回search.jsp页面
 *
 * @ClassName SearchController
 * @Description TODO
 * @Author eooy
 * @Date 2018/5/9 22:16
 * @Version 1.0
 **/
@Controller
public class SearchController {
    @Autowired
    private SearchService searchService;
    //每页显示的商品数
    @Value("${PAGE_ROWS}")
    private Integer PAGE_ROWS;

    //<form action="http://localhost:8076/search.html" id="searchForm" name="query" method="GET">
    @RequestMapping(value = "/search",method = RequestMethod.GET)
    public String search(String keyword, @RequestParam(defaultValue = "1") Integer page, Model model) throws Exception {
        //Tomcat8之后get的参数不需要转码了
       // keyword = new String(keyword.getBytes("iso8859-1"), "utf-8");
        SearchResult searchResult = searchService.search(keyword, page,PAGE_ROWS);
        //把结果传递给jsp页面
        model.addAttribute("query", keyword);
        model.addAttribute("totalPages", searchResult.getTotalPages());
        model.addAttribute("recourdCount", searchResult.getRecourdCount());
        model.addAttribute("page", page);
        model.addAttribute("itemList", searchResult.getItemList());

        return "search";
    }
}
