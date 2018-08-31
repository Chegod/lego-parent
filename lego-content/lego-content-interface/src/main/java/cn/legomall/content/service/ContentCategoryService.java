package cn.legomall.content.service;


import cn.legomall.common.pojo.EasyUITreeNode;
import cn.legomall.common.pojo.LeGoResult;

import java.util.List;

/**商城内容分类service
 * @ClassName ContentCategoryService
 * @Description TODO
 * @Author eooy
 * @Date 2018/5/2 17:05
 * @Version 1.0
 **/
public interface ContentCategoryService {
    //查询tb_content_category表,返回初始化easyUITree需要的节点列表
    List<EasyUITreeNode> getContentCatList(Long parentId);
    //添加商城内容分类的节点
    LeGoResult addContentCategory(Long parentId, String name);
    //重命名(更新)商城内容分类的节点
    LeGoResult updateContentCategory(Long id, String name);
    //删除商城内容分类的节点
    LeGoResult deleteContentCategory(Long id);
}
