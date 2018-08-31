package cn.legomall.search.service;

import cn.legomall.common.pojo.LeGoResult;


/**将从数据库中查到的商品数据导入solr索引库
 * @ClassName SearchItemService
 * @Description TODO
 * @Author eooy
 * @Date 2018/5/9 15:39
 * @Version 1.0
 **/
public interface SearchItemService {
    LeGoResult importAllItem();
    LeGoResult addDocument(Long itemId) throws Exception;
}
