package cn.legomall.solrj;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 测试solrJ
 *
 * @ClassName SolrJTest
 * @Description TODO
 * @Author eooy
 * @Date 2018/5/8 21:25
 * @Version 1.0
 **/
public class SolrJTest {
    /**
     * 不同Solr 版本之间创建连接方式不同
     * Solr4 ： SolrServer solrServer = new HttpSolrServer("http://localhost:8080/solr");
     * Solr5 ：HttpSolrClient solrClient = new HttpSolrClient("http://localhost:8080/solr/new_core");
     * Solr7 ：HttpSolrClient solrClient = new HttpSolrClient.Builder("http://localhost:8080/solr/new_core").build();
     * <p>
     * SolrQuery 对应的api接口
     * q : setQuery
     * fq : addFilterQuery
     * sort : setSort
     * start : setStart(0);
     * rows : setRows(10);
     * fl : setFields
     */

    private final static String BASE_SOLR_URL = "http://118.24.93.102:8983/solr/new_core";

    // solrJ 基础用法
    @Test
    public void queryDocumentBasic() throws Exception {
        // 创建连接
        HttpSolrClient solrClient = new HttpSolrClient.Builder(BASE_SOLR_URL).build();

        /* 不推荐
        Map<String, String> queryParamMap = new HashMap<String, String>();
        // 封装查询参数
        queryParamMap.put("q", "*:*");
        // 添加到SolrParams对象
        MapSolrParams query = new MapSolrParams(queryParamMap);
        */

        // 设置查询条件
        SolrQuery query = new SolrQuery();
        query.setQuery("*:*");
        // 执行查询
        QueryResponse response = solrClient.query(query);
        // 获取doc文档
        SolrDocumentList documents = response.getResults();
        System.out.println("共查询到记录：" + documents.getNumFound());
        for (SolrDocument solrDocument : documents) {
            System.out.println("id:" + solrDocument.get("id"));
            System.out.println("item_title:" + solrDocument.get("item_title"));
            System.out.println("item_price:" + solrDocument.get("item_price"));

        }
    }

    // solrJ 的复杂查询
    @Test
    public void queryDocument() throws Exception {
        // 创建连接
        HttpSolrClient solrClient = new HttpSolrClient.Builder(BASE_SOLR_URL).build();
        SolrQuery query = new SolrQuery();
        // 设置查询条件
        query.setQuery("item_title:测试商品2");
        // 设置分页信息
        query.setStart(0);
        query.setRows(10);
        // 设置排序,desc降序排序(越来越小)
        query.setSort("id", SolrQuery.ORDER.desc);
        // 设置显示的Field的域集合,即设置那些值有返回值
        query.setFields("id,item_title");
        // 设置默认域
        query.set("df", "item_keywords");
        // 设置高亮信息
        query.setHighlight(true);
        query.addHighlightField("item_title");
        query.setHighlightSimplePre("<em>");
        query.setHighlightSimplePost("</em>");
        // 执行查询
        QueryResponse response = solrClient.query(query);
        // 获取doc文档
        SolrDocumentList documents = response.getResults();
        System.out.println("共查询到记录：" + documents.getNumFound());
        // 获取高亮显示信息
        //Map<String(文档id), Map<String(要高亮显示的域的名称), List<String>>> highlighting = response.getHighlighting();
        Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
        for (SolrDocument doc : documents) {
            System.out.println(doc.get("id"));
            List<String> hightDocs = highlighting.get(doc.get("id")).get("item_title");
            if (hightDocs != null&&hightDocs.size()>0)
                //hightDocs.get(0)有的话,就只有一条
                System.out.println("高亮显示的商品名称：" + hightDocs.get(0));
            else {
                System.out.println(doc.get("item_title"));
            }
        }
    }

    // 添加索引
    @Test
    public void addDocuments() throws SolrServerException, IOException {
        HttpSolrClient solrClient = new HttpSolrClient.Builder(BASE_SOLR_URL)
                .withConnectionTimeout(10000)
                .withSocketTimeout(60000)
                .build();
        for (int i = 1; i < 4; i++) {
            // 创建一个文档对象
            SolrInputDocument document = new SolrInputDocument();
            document.addField("id", "add-00"+i);
            document.addField("item_title", "测试商品"+i);
            document.addField("item_price", i+"00");
            // 将文档对象写入到索引库中
            solrClient.add(document);
        }
        // 提交
        solrClient.commit();
    }

    // 更新的逻辑：先通过id将直接的数据删掉，然后再创建，所以update 和 create 是同一代码

    // 删除/批量删除索引
    @Test
    public void deleteDocument() throws SolrServerException, IOException {
        //Builder是HttpSolrClient的静态内部类
        HttpSolrClient solrClient = new HttpSolrClient.Builder(BASE_SOLR_URL).build();
        //solrClient.deleteById("add-001"); 通过id删除
        /* 批量删除
        ArrayList<String> ids = new ArrayList<String>();
        ids.add("1");
        ids.add("2");
        solrClient.deleteById(ids);
        */
        //搜索过程就是在索引上查找域为id，并且关键字为add-001的term，并根据term找到文档id列表。
        solrClient.deleteByQuery("id:add-001"); // 通过查询条件删除
        solrClient.commit();
    }
}
