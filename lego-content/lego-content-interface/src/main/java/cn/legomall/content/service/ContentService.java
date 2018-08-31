package cn.legomall.content.service;

import cn.legomall.common.pojo.EasyUIDataGridResult;
import cn.legomall.common.pojo.LeGoResult;
import cn.legomall.pojo.TbContent;

import java.util.List;

/**商城内容service
 * @ClassName ContentService
 * @Description TODO
 * @Author eooy
 * @Date 2018/5/3 17:40
 * @Version 1.0
 **/
public interface ContentService {

    //根据内容分类id查询内容列表,要进行分页处理,page:页面当前页,rows:每一页显示多少条
    EasyUIDataGridResult getContentList(Long categoryId, Integer page, Integer rows);
    //点击content.jsp中的新增按钮,会弹出一个窗口显示content-add.jsp
    LeGoResult addContent(TbContent content);
    //动态显示商城首页
    List<TbContent> getContentListByCid(Long categoryId);
}
