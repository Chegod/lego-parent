package cn.legomall.search.mapper;

import cn.legomall.common.pojo.SearchItem;

import java.util.List;

/**用于创建mybatis代理对象的接口
 * @ClassName ItemMapper
 * @Description TODO
 * @Author eooy
 * @Date 2018/5/9 14:53
 * @Version 1.0
 **/
public interface ItemMapper {
    List<SearchItem>  getSearchItemList();
    SearchItem getSearchItemById(Long itemId);
}
