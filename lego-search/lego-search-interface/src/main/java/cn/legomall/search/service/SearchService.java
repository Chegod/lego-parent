package cn.legomall.search.service;

import cn.legomall.common.pojo.SearchResult;

/**到solr索引库中查询数据,封装SearchResult对象
 * @ClassName SearchService
 * @Description TODO
 * @Author eooy
 * @Date 2018/5/9 22:49
 * @Version 1.0
 **/
public interface SearchService {
    SearchResult search(String keyword, Integer page, Integer page_rows)throws Exception;
}
