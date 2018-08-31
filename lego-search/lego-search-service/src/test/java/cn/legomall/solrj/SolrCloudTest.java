package cn.legomall.solrj;


import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.common.SolrInputDocument;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * SolrCloud测试
 *
 * @ClassName SolrCloudTest
 * @Description TODO
 * @Author eooy
 * @Date 2018/5/11 16:01
 * @Version 1.0
 **/
public class SolrCloudTest {

    public void solrCloudAddDocumentTest() throws IOException, SolrServerException {
        List<String> zkHosts = new ArrayList<>();
        //zookeeper在三台服务器上
        zkHosts.add("1.1.1.1:2181");
        zkHosts.add("1.1.1.2:2181");
        zkHosts.add("1.1.1.3:2181");
        //已过时CloudSolrClient cloudSolrClient = new CloudSolrClient.Builder().withZkHost(zkHosts).build();
        //创建CloudSolrClient同时指定超时时间，不指定走默认配置
        CloudSolrClient cloudSolrClient = new CloudSolrClient.Builder(zkHosts, null)
                .withConnectionTimeout(10000)
                .withSocketTimeout(60000)
                .build();
        cloudSolrClient.setDefaultCollection("collection2");
        //创建一SolrInputDocument对象。
        SolrInputDocument document = new SolrInputDocument();
        //向文档对象中添加域
        document.addField("item_title", "测试商品");
        document.addField("item_price", "100");
        document.addField("id", "test001");
        //把文档对象写入索引库。
        cloudSolrClient.add(document);
        //提交。
        cloudSolrClient.commit();

    }
}
