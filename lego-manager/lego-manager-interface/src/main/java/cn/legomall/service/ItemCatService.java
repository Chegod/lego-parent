package cn.legomall.service;


import cn.legomall.common.pojo.EasyUITreeNode;

import java.util.List;

/**
 * 商品类目结构service
 * @ClassName ItemCatService
 * @Description TODO
 * @Author eooy
 * @Date 2018/4/27 16:43
 * @Version 1.0
 **/
public interface ItemCatService {
    List<EasyUITreeNode> getItemCatList(Long parentId);
}
