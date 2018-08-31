package cn.legomall.search.service.impl;

import cn.legomall.common.pojo.SearchResult;
import cn.legomall.search.dao.SearchDao;
import cn.legomall.search.service.SearchService;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * 到solr索引库中查询数据,封装SearchResult对象
 * @ClassName SearchServiceImpl
 * @Description TODO
 * @Author eooy
 * @Date 2018/5/9 22:55
 * @Version 1.0
 **/
@Service
public class SearchServiceImpl implements SearchService {
        @Autowired
        private SearchDao searchDao;

        @Value("${DEFAULT_FIELD}")
        private String DEFAULT_FIELD;

        @Override
        public SearchResult search(String keyword, Integer page, Integer page_rows) throws Exception {
            //创建一个SolrQuery对象
            SolrQuery query = new SolrQuery();
            //设置查询条件
            query.setQuery(keyword);
            //设置分页条件
            query.setStart((page - 1) * page_rows);
            //设置rows
            query.setRows(page_rows);
            //设置默认搜索域
            query.set("df", DEFAULT_FIELD);
            //设置高亮显示
            query.setHighlight(true);
            query.addHighlightField("item_title");
            query.setHighlightSimplePre("<em style=\"color:red\">");
            query.setHighlightSimplePost("</em>");
            //执行查询
            SearchResult searchResult = searchDao.search(query);
            //计算总页数
            int recourdCount = searchResult.getRecourdCount();
            int pages = recourdCount / page_rows;
            if (recourdCount % page_rows > 0) pages++;
            //设置到返回结果
            searchResult.setTotalPages(pages);
            return searchResult;
        }
}
