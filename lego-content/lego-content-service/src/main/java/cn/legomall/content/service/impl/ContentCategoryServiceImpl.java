package cn.legomall.content.service.impl;

import cn.legomall.common.pojo.EasyUITreeNode;
import cn.legomall.common.pojo.LeGoResult;
import cn.legomall.content.service.ContentCategoryService;
import cn.legomall.mapper.TbContentCategoryMapper;
import cn.legomall.pojo.TbContentCategory;
import cn.legomall.pojo.TbContentCategoryExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**商城内容分类ServiceImpl
 * @ClassName ContentCategoryServiceImpl
 * @Description TODO
 * @Author eooy
 * @Date 2018/5/2 17:12
 * @Version 1.0
 **/
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {
    @Autowired
    private TbContentCategoryMapper tbContentCategoryMapper;

    //查询tb_content_category表,返回初始化easyUITree需要的节点列表
    @Override
    public List<EasyUITreeNode> getContentCatList(Long parentId) {
        //查询数据库
        TbContentCategoryExample example = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        //一开始只要一个TbContentCategory对象,它是根节点,当点击根节点时,根节点会再次请求,并且带上自己的id
        List<TbContentCategory> contentCategoryList = tbContentCategoryMapper.selectByExample(example);
        //封装EasyUITreeNode
        List<EasyUITreeNode> treeNodeList = new ArrayList<>();
        for(TbContentCategory contentCategory:contentCategoryList){
            EasyUITreeNode treeNode = new EasyUITreeNode();
            treeNode.setId(contentCategory.getId());
            treeNode.setText(contentCategory.getName());
            treeNode.setState(contentCategory.getIsParent()?"closed":"open");
            treeNodeList.add(treeNode);
        }
        return treeNodeList;
    }

    //添加商城内容分类的节点
    @Override
    public LeGoResult addContentCategory(Long parentId, String name) {
        //创建一个要添加的contentCategory对象,补全属性
        TbContentCategory tbContentCategory = new TbContentCategory();
        // b)补全TbContentCategory对象的属性
        tbContentCategory.setIsParent(false);
        tbContentCategory.setName(name);
        tbContentCategory.setParentId(parentId);
        //排列序号，表示同级类目的展现次序，如数值相等则按名称次序排列。取值范围:大于零的整数
        tbContentCategory.setSortOrder(1);
        //状态。可选值:1(正常),2(删除)
        tbContentCategory.setStatus(1);
        tbContentCategory.setCreated(new Date());
        tbContentCategory.setUpdated(new Date());
        //插入节点,设置主键返回,返回的主键自动封装到了tbContentCategory中了
        tbContentCategoryMapper.insert(tbContentCategory);
        //查询此节点的父节点
        TbContentCategory parentNode = tbContentCategoryMapper.selectByPrimaryKey(parentId);
        //更新父节点的状态
        if(!parentNode.getIsParent()){
            parentNode.setIsParent(true);
            tbContentCategoryMapper.updateByPrimaryKey(parentNode);
        }
        LeGoResult ok = LeGoResult.ok(tbContentCategory);
        return ok;
    }
    //重命名(更新)商城内容分类的节点
    @Override
    public LeGoResult updateContentCategory(Long id, String name) {
        //创建一个TbContentCategory对象
        TbContentCategory contentCategory = new TbContentCategory();
        //重命名
        contentCategory.setId(id);
        contentCategory.setName(name);
        //更新到数据库
        tbContentCategoryMapper.updateByPrimaryKeySelective(contentCategory);

        return LeGoResult.ok();
    }
    //删除商城内容分类的节点
    @Override
    public LeGoResult deleteContentCategory(Long id) {
        TbContentCategory contentCategory = tbContentCategoryMapper.selectByPrimaryKey(id);
        Long parentId = contentCategory.getParentId();
        TbContentCategoryExample example = new TbContentCategoryExample();
        //删除分子节点和父节点
        if(contentCategory.getIsParent()){
            //是父节点的话,它下面一定有子节点,所以要级联删除
            TbContentCategoryExample.Criteria criteria = example.createCriteria();
            criteria.andParentIdEqualTo(contentCategory.getId());
            //删除该父节点下的所有子节点
            tbContentCategoryMapper.deleteByExample(example);
            //删除父节点
            tbContentCategoryMapper.deleteByPrimaryKey(id);

        }else{
            //是子节点
            tbContentCategoryMapper.deleteByPrimaryKey(id);
            //删除子节点后要判断该父节点下是否还有其他子节点
            TbContentCategoryExample.Criteria criteria = example.createCriteria();
            criteria.andParentIdEqualTo(parentId);
            int count = tbContentCategoryMapper.countByExample(example);
            if(count==0){
                //判断父节点下是否还有子节点，如果没有需要把父节点的isparent改为false
                TbContentCategory contentCategoryParent = new TbContentCategory();
                contentCategoryParent.setId(parentId);
                contentCategoryParent.setIsParent(false);
                tbContentCategoryMapper.updateByPrimaryKeySelective(contentCategoryParent);
            }
        }
        return LeGoResult.ok();
    }
}

