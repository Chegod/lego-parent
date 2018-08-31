package cn.legomall.service.impl;

import cn.legomall.common.pojo.EasyUITreeNode;
import cn.legomall.mapper.TbItemCatMapper;
import cn.legomall.pojo.TbItemCat;
import cn.legomall.pojo.TbItemCatExample;
import cn.legomall.pojo.TbItemCatExample.Criteria;
import cn.legomall.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品类目结构service
 * @ClassName ItemCatServiceImpl
 * @Description TODO
 * @Author eooy
 * @Date 2018/4/27 16:55
 * @Version 1.0
 **/
@Service
public  class ItemCatServiceImpl implements ItemCatService {

    @Autowired
    private TbItemCatMapper tbItemCatMapper;

    @Override
    public List<EasyUITreeNode> getItemCatList(Long parentId) {
        //根据父节点id查询父节点下的子节点
        TbItemCatExample example = new TbItemCatExample();
        Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        List<TbItemCat> itemCatList = tbItemCatMapper.selectByExample(example);
        //封装EasyUITreeNode
        List<EasyUITreeNode> treeNodeList = new ArrayList<EasyUITreeNode>();
        for (TbItemCat itemCat:itemCatList) {
            EasyUITreeNode treeNode = new EasyUITreeNode();
            //节点id就是tb_item_cat表中的id
            treeNode.setId(itemCat.getId());
            //节点name就是tb_item_cat表中的name
            treeNode.setText(itemCat.getName());
            //state:节点状态，'open' 或 'closed'，默认：'open'。如果为'closed'的时候，将不自动展开该节点。
            treeNode.setState(itemCat.getIsParent()?"closed":"open");
            treeNodeList.add(treeNode);
        }
        return treeNodeList;
    }
}
