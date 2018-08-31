package cn.legomall.search.service.impl;

import cn.legomall.common.pojo.LeGoResult;
import cn.legomall.common.pojo.SearchItem;
import cn.legomall.search.mapper.ItemMapper;
import cn.legomall.search.service.SearchItemService;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * 将从数据库中查到的商品数据导入solr索引库
 *
 * @ClassName SearchItemServiceImpl
 * @Description TODO
 * @Author eooy
 * @Date 2018/5/9 15:42
 * @Version 1.0
 **/
@Service
public class SearchItemServiceImpl implements SearchItemService {
    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private HttpSolrClient solrClient;

    //将商品数据导入索引库中
    @Override
    public LeGoResult importAllItem() {
        try {
        //从数据库中查询商品数据
        List<SearchItem> searchItemList = itemMapper.getSearchItemList();
        //将查询到的数据导入索引库中,一条数据(SearchItem)是一个document
        for (SearchItem searchItem : searchItemList) {
            //创建一个文档对象
            SolrInputDocument document = new SolrInputDocument();
            //向文档中的filed域添加数据
            document.addField("id",searchItem.getId());
            //<field name="item_title"
            document.addField("item_title",searchItem.getTitle());
             //<field name="item_sell_point"
            document.addField("item_sell_point",searchItem.getSell_point());
            //<field name="item_price"
            document.addField("item_price",searchItem.getPrice());
            //<field name="item_image"
            document.addField("item_image",searchItem.getImage());
            //<field name="item_category_name"
            document.addField("item_category_name",searchItem.getCategory_name());
            solrClient.add(document);
        }
            //提交document
            solrClient.commit();
            //提交成功
            return LeGoResult.ok();
            } catch (Exception e) {
                e.printStackTrace();
                //提交失败
                return LeGoResult.build(500,"导入索引库失败");
            }
    }

    //将添加的一件商品从数据库中查出然后添加到solr中
    @Override
    public LeGoResult addDocument(Long itemId) throws IOException, SolrServerException {
        //根据id查询商品
        SearchItem searchItem = itemMapper.getSearchItemById(itemId);
        //将查询到的商品添加到索引库中
        SolrInputDocument document = new SolrInputDocument();
        //为这个document添加field
        document.addField("id",searchItem.getId());
        //<field name="item_title"
        document.addField("item_title",searchItem.getTitle());
        //<field name="item_sell_point"
        document.addField("item_sell_point",searchItem.getSell_point());
        //<field name="item_price"
        document.addField("item_price",searchItem.getPrice());
        //<field name="item_image"
        document.addField("item_image",searchItem.getImage());
        //<field name="item_category_name"
        document.addField("item_category_name",searchItem.getCategory_name());
        solrClient.add(document);
        solrClient.commit();
        return LeGoResult.ok();
    }
}
